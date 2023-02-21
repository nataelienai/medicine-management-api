package br.com.memory.projetoavaliacao.adversereaction;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;

@DataJpaTest
public class AdverseReactionRepositoryTest {
  @Autowired
  private AdverseReactionRepository adverseReactionRepository;

  @BeforeEach
  void cleanUp() {
    adverseReactionRepository.deleteAll();
  }

  @Test
  @DisplayName("findAllBy() should return all adverse reactions when not given description and pagination")
  void findAllByShouldReturnAllAdverseReactionsWhenNotGivenDescriptionAndPagination() {
    // given
    Set<AdverseReaction> adverseReactions = Set.of(
        new AdverseReaction("Bad reaction"),
        new AdverseReaction("Very bad reaction"),
        new AdverseReaction("Worst reaction"));
    adverseReactionRepository.saveAll(adverseReactions);

    // when
    Page<AdverseReaction> retrievedAdverseReactions = adverseReactionRepository.findAllBy(null, null);

    // then
    assertThat(retrievedAdverseReactions.getTotalElements()).isEqualTo(adverseReactions.size());
    assertThat(retrievedAdverseReactions.getNumberOfElements()).isEqualTo(adverseReactions.size());
  }
}
