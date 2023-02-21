package br.com.memory.projetoavaliacao.adversereaction;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.memory.projetoavaliacao.medicine.MedicineRepository;

@ExtendWith(MockitoExtension.class)
public class AdverseReactionServiceTest {
  @Mock
  private AdverseReactionRepository adverseReactionRepository;

  @Mock
  private MedicineRepository medicineRepository;

  private AdverseReactionService adverseReactionService;

  @BeforeEach
  void setUp() {
    adverseReactionService = new AdverseReactionService(adverseReactionRepository, medicineRepository);
  }

  @Test
  @DisplayName("findAllBy() should call adverse reaction repository with the given arguments")
  void findAllByShouldCallAdverseReactionRepositoryWithTheGivenArguments() {
    // given
    String description = "some description";
    Pageable page = PageRequest.of(0, 1);

    // when
    adverseReactionService.findAllBy(description, page);

    // then
    verify(adverseReactionRepository).findAllBy(description, page);
  }
}
