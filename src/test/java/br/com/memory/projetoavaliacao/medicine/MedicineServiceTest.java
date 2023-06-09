package br.com.memory.projetoavaliacao.medicine;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.memory.projetoavaliacao.adversereaction.AdverseReaction;
import br.com.memory.projetoavaliacao.adversereaction.AdverseReactionRepository;
import br.com.memory.projetoavaliacao.manufacturer.Manufacturer;
import br.com.memory.projetoavaliacao.manufacturer.ManufacturerRepository;
import br.com.memory.projetoavaliacao.shared.exception.ResourceAlreadyExistsException;
import br.com.memory.projetoavaliacao.shared.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class MedicineServiceTest {
  private MedicineService medicineService;

  @Mock
  private MedicineRepository medicineRepository;

  @Mock
  private ManufacturerRepository manufacturerRepository;

  @Mock
  private AdverseReactionRepository adverseReactionRepository;

  @BeforeEach
  void setUp() {
    medicineService = new MedicineService(
        medicineRepository,
        manufacturerRepository,
        adverseReactionRepository);
  }

  @Test
  @DisplayName("findAllBy() should call medicine repository with the given arguments")
  void findAllByShouldCallMedicineRepositoryWithGivenArguments() {
    // given
    String registrationNumber = "1.4444.4444.333-1";
    String name = "Strong medicine";
    Pageable page = PageRequest.of(0, 1);

    // when
    medicineService.findAllBy(registrationNumber, name, page);

    // then
    verify(medicineRepository).findAllBy(registrationNumber, name, page);
  }

  @Test
  @DisplayName("create() should throw when the given registration number already exists")
  void createShouldThrowWhenGivenRegistrationNumberAlreadyExists() {
    // given
    MedicineCreationDto medicineCreationDto = makeMedicineCreationDto(1L, Set.of(1L));
    when(medicineRepository.existsById(medicineCreationDto.getRegistrationNumber()))
        .thenReturn(true);

    // when
    // then
    assertThatThrownBy(() -> medicineService.create(medicineCreationDto))
        .isInstanceOf(ResourceAlreadyExistsException.class);

    verify(medicineRepository, never()).save(any());
  }

  @Test
  @DisplayName("create() should throw when given a non-existent manufacturer id")
  void createShouldThrowWhenGivenNonExistentManufacturerId() {
    // given
    MedicineCreationDto medicineCreationDto = makeMedicineCreationDto(1L, Set.of(1L));
    when(medicineRepository.existsById(medicineCreationDto.getRegistrationNumber()))
        .thenReturn(false);
    when(manufacturerRepository.findById(medicineCreationDto.getManufacturerId()))
        .thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> medicineService.create(medicineCreationDto))
        .isInstanceOf(ResourceNotFoundException.class);

    verify(medicineRepository, never()).save(any());
  }

  @Test
  @DisplayName("create() should throw when given a non-existent adverse reaction id")
  void createShouldThrowWhenGivenNonExistentAdverseReactionId() {
    // given
    Manufacturer manufacturer = makeManufacturer();
    MedicineCreationDto medicineCreationDto = makeMedicineCreationDto(
        manufacturer.getId(),
        Set.of(1L));

    when(medicineRepository.existsById(medicineCreationDto.getRegistrationNumber()))
        .thenReturn(false);
    when(manufacturerRepository.findById(medicineCreationDto.getManufacturerId()))
        .thenReturn(Optional.of(manufacturer));
    when(adverseReactionRepository.findAllById(medicineCreationDto.getAdverseReactionIds()))
        .thenReturn(List.of());

    // when
    // then
    assertThatThrownBy(() -> medicineService.create(medicineCreationDto))
        .isInstanceOf(ResourceNotFoundException.class);

    verify(medicineRepository, never()).save(any());
  }

  @Test
  @DisplayName("create() should create a medicine when given an entirely valid input")
  void createShouldCreateMedicineWhenGivenEntirelyValidInput() {
    // given
    Manufacturer manufacturer = makeManufacturer();
    AdverseReaction adverseReaction = makeAdverseReaction();
    MedicineCreationDto medicineCreationDto = makeMedicineCreationDto(
        manufacturer.getId(),
        Set.of(adverseReaction.getId()));

    when(medicineRepository.existsById(medicineCreationDto.getRegistrationNumber()))
        .thenReturn(false);
    when(manufacturerRepository.findById(medicineCreationDto.getManufacturerId()))
        .thenReturn(Optional.of(manufacturer));
    when(adverseReactionRepository.findAllById(medicineCreationDto.getAdverseReactionIds()))
        .thenReturn(List.of(adverseReaction));
    when(medicineRepository.save(any(Medicine.class))).then(returnsFirstArg());

    // when
    Medicine medicine = medicineService.create(medicineCreationDto);

    // then
    assertThat(medicine).isNotNull();
    assertThat(medicine)
        .usingRecursiveComparison()
        .ignoringExpectedNullFields()
        .isEqualTo(medicineCreationDto);
    assertThat(medicine.getManufacturer()).isEqualTo(manufacturer);
    assertThat(medicine.getAdverseReactions().contains(adverseReaction)).isTrue();
  }

  @Test
  @DisplayName("update() should throw when given a non-existent registration number")
  void updateShouldThrowWhenGivenNonExistentRegistrationNumber() {
    // given
    String registrationNumber = "1.4444.4444.333-1";
    MedicineUpdateDto medicineUpdateDto = makeMedicineUpdateDto(1L, Set.of(1L));
    when(medicineRepository.findById(registrationNumber))
        .thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> medicineService.update(registrationNumber, medicineUpdateDto))
        .isInstanceOf(ResourceNotFoundException.class);

    verify(medicineRepository, never()).save(any());
  }

  @Test
  @DisplayName("update() should throw when given a non-existent manufacturer id")
  void updateShouldThrowWhenGivenNonExistentManufacturerId() {
    // given
    Medicine medicine = makeMedicine(makeManufacturer(), Set.of(makeAdverseReaction()));
    String registrationNumber = medicine.getRegistrationNumber();
    MedicineUpdateDto medicineUpdateDto = makeMedicineUpdateDto(0L, Set.of(0L));

    when(medicineRepository.findById(registrationNumber))
        .thenReturn(Optional.of(medicine));
    when(manufacturerRepository.findById(medicineUpdateDto.getManufacturerId()))
        .thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> medicineService.update(registrationNumber, medicineUpdateDto))
        .isInstanceOf(ResourceNotFoundException.class);

    verify(medicineRepository, never()).save(any());
  }

  @Test
  @DisplayName("update() should throw when given a non-existent adverse reaction id")
  void updateShouldThrowWhenGivenNonExistentAdverseReactionId() {
    // given
    Manufacturer manufacturer = makeManufacturer();
    Medicine medicine = makeMedicine(manufacturer, Set.of(makeAdverseReaction()));
    String registrationNumber = medicine.getRegistrationNumber();
    MedicineUpdateDto medicineUpdateDto = makeMedicineUpdateDto(
        manufacturer.getId(),
        Set.of(0L));

    when(medicineRepository.findById(registrationNumber))
        .thenReturn(Optional.of(medicine));
    when(manufacturerRepository.findById(medicineUpdateDto.getManufacturerId()))
        .thenReturn(Optional.of(manufacturer));
    when(adverseReactionRepository.findAllById(medicineUpdateDto.getAdverseReactionIds()))
        .thenReturn(List.of());

    // when
    // then
    assertThatThrownBy(() -> medicineService.update(registrationNumber, medicineUpdateDto))
        .isInstanceOf(ResourceNotFoundException.class);

    verify(medicineRepository, never()).save(any());
  }

  @Test
  @DisplayName("update() should update a medicine when given an entirely valid input")
  void updateShouldUpdateMedicineWhenGivenEntirelyValidInput() {
    // given
    Manufacturer manufacturer = makeManufacturer();
    AdverseReaction adverseReaction = makeAdverseReaction();
    Medicine medicine = makeMedicine(manufacturer, Set.of(adverseReaction));
    String registrationNumber = medicine.getRegistrationNumber();
    MedicineUpdateDto medicineUpdateDto = makeMedicineUpdateDto(
        manufacturer.getId(),
        Set.of(adverseReaction.getId()));

    when(medicineRepository.findById(registrationNumber))
        .thenReturn(Optional.of(medicine));
    when(manufacturerRepository.findById(medicineUpdateDto.getManufacturerId()))
        .thenReturn(Optional.of(manufacturer));
    when(adverseReactionRepository.findAllById(medicineUpdateDto.getAdverseReactionIds()))
        .thenReturn(List.of(adverseReaction));
    when(medicineRepository.save(any(Medicine.class))).then(returnsFirstArg());

    // when
    Medicine updatedMedicine = medicineService.update(registrationNumber, medicineUpdateDto);

    // then
    assertThat(updatedMedicine).isNotNull();
    assertThat(updatedMedicine)
        .usingRecursiveComparison()
        .ignoringExpectedNullFields()
        .isEqualTo(medicineUpdateDto);
    assertThat(updatedMedicine.getRegistrationNumber()).isEqualTo(registrationNumber);
    assertThat(updatedMedicine.getManufacturer()).isEqualTo(manufacturer);
    assertThat(updatedMedicine.getAdverseReactions().contains(adverseReaction)).isTrue();
  }

  @Test
  @DisplayName("deleteByRegistrationNumber() should throw when given a non-existent registration number")
  void deleteByRegistrationNumberShouldThrowWhenGivenNonExistentRegistrationNumber() {
    // given
    String registrationNumber = "1.4444.4444.333-1";

    // when
    // then
    assertThatThrownBy(() -> medicineService.deleteByRegistrationNumber(registrationNumber))
        .isInstanceOf(ResourceNotFoundException.class);

    verify(medicineRepository, never()).delete(any());
  }

  @Test
  @DisplayName("deleteByRegistrationNumber() should delete medicine when given an existent registration number")
  void deleteByRegistrationNumberShouldDeleteMedicineWhenGivenExistentRegistrationNumber() {
    // given
    Manufacturer manufacturer = makeManufacturer();
    AdverseReaction adverseReaction = makeAdverseReaction();
    Medicine medicine = makeMedicine(manufacturer, Set.of(adverseReaction));

    when(medicineRepository.findById(medicine.getRegistrationNumber()))
        .thenReturn(Optional.of(medicine));

    // when
    medicineService.deleteByRegistrationNumber(medicine.getRegistrationNumber());

    // then
    verify(medicineRepository).delete(medicine);
  }

  private Manufacturer makeManufacturer() {
    return new Manufacturer(1L, "Manufacturer");
  }

  private AdverseReaction makeAdverseReaction() {
    return new AdverseReaction(1L, "Description");
  }

  private MedicineCreationDto makeMedicineCreationDto(Long manufacturerId, Set<Long> adverseReactionIds) {
    return new MedicineCreationDto(
        "1.4444.4444.333-1",
        "medicine",
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        1,
        manufacturerId,
        adverseReactionIds);
  }

  private MedicineUpdateDto makeMedicineUpdateDto(Long manufacturerId, Set<Long> adverseReactionIds) {
    return new MedicineUpdateDto(
        "updated medicine",
        LocalDate.now(),
        "(12)1111-1111",
        BigDecimal.valueOf(2),
        2,
        manufacturerId,
        adverseReactionIds);
  }

  private Medicine makeMedicine(Manufacturer manufacturer, Set<AdverseReaction> adverseReaction) {
    return new Medicine(
        "1.4444.4444.333-1",
        "medicine",
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        1,
        manufacturer,
        adverseReaction);
  }
}
