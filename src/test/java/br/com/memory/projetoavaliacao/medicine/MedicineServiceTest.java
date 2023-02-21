package br.com.memory.projetoavaliacao.medicine;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.memory.projetoavaliacao.adversereaction.AdverseReactionRepository;
import br.com.memory.projetoavaliacao.manufacturer.ManufacturerRepository;

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
}
