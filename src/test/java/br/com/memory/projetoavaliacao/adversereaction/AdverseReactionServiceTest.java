package br.com.memory.projetoavaliacao.adversereaction;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.memory.projetoavaliacao.medicine.MedicineRepository;
import br.com.memory.projetoavaliacao.shared.exception.ResourceNotFoundException;

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

  @Test
  @DisplayName("create() should create a new adverse reaction and return it")
  void createShouldCreateNewAdverseReactionAndReturnIt() {
    // given
    AdverseReactionDto adverseReactionDto = new AdverseReactionDto("description");
    when(adverseReactionRepository.save(any(AdverseReaction.class)))
        .then(returnsFirstArg());

    // when
    AdverseReaction adverseReaction = adverseReactionService.create(adverseReactionDto);

    // then
    assertThat(adverseReaction).isNotNull();
    assertThat(adverseReaction.getDescription()).isEqualTo(adverseReactionDto.getDescription());
  }

  @Test
  @DisplayName("update() should throw when given a non-existent id")
  void updateShouldThrowWhenGivenNonExistentId() {
    // given
    Long id = 1L;
    AdverseReactionDto adverseReactionDto = new AdverseReactionDto("description");
    when(adverseReactionRepository.findById(id)).thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> adverseReactionService.update(id, adverseReactionDto))
        .isInstanceOf(ResourceNotFoundException.class);

    verify(adverseReactionRepository, never()).save(any());
  }

  @Test
  @DisplayName("update() should update adverse reaction when given an existent id")
  void updateShouldUpdateAdverseReactionWhenGivenExistentId() {
    // given
    Long id = 1L;
    AdverseReaction adverseReaction = new AdverseReaction(id, "description");
    when(adverseReactionRepository.findById(id))
        .thenReturn(Optional.of(adverseReaction));

    when(adverseReactionRepository.save(any(AdverseReaction.class)))
        .then(returnsFirstArg());

    AdverseReactionDto adverseReactionDto = new AdverseReactionDto("updated description");

    // when
    AdverseReaction updatedAdverseReaction = adverseReactionService.update(id, adverseReactionDto);

    // then
    assertThat(updatedAdverseReaction).isNotNull();
    assertThat(updatedAdverseReaction.getId()).isEqualTo(id);
    assertThat(updatedAdverseReaction.getDescription()).isEqualTo(adverseReactionDto.getDescription());
  }
}
