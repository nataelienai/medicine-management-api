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
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "1.4444.4444.333-1",
        "medicine",
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        1,
        1L,
        Set.of(1L));
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
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "1.4444.4444.333-1",
        "medicine",
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        1,
        1L,
        Set.of(1L));
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
    Manufacturer manufacturer = new Manufacturer(1L, "manufacturer");
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "1.4444.4444.333-1",
        "medicine",
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        1,
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
    Manufacturer manufacturer = new Manufacturer(1L, "manufacturer");
    AdverseReaction adverseReaction = new AdverseReaction(1L, "description");
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "1.4444.4444.333-1",
        "medicine",
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        1,
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
    assertThat(medicine.getRegistrationNumber()).isEqualTo(medicineCreationDto.getRegistrationNumber());
    assertThat(medicine.getName()).isEqualTo(medicineCreationDto.getName());
    assertThat(medicine.getExpirationDate()).isEqualTo(medicineCreationDto.getExpirationDate());
    assertThat(medicine.getCustomerServicePhone()).isEqualTo(medicineCreationDto.getCustomerServicePhone());
    assertThat(medicine.getPrice()).isEqualTo(medicineCreationDto.getPrice());
    assertThat(medicine.getAmountOfPills()).isEqualTo(medicineCreationDto.getAmountOfPills());
    assertThat(medicine.getManufacturer()).isEqualTo(manufacturer);
    assertThat(medicine.getAdverseReactions().contains(adverseReaction)).isTrue();
  }
}
