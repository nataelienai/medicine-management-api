package br.com.memory.projetoavaliacao.medicine;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.memory.projetoavaliacao.adversereaction.AdverseReaction;
import br.com.memory.projetoavaliacao.adversereaction.AdverseReactionRepository;
import br.com.memory.projetoavaliacao.manufacturer.Manufacturer;
import br.com.memory.projetoavaliacao.manufacturer.ManufacturerRepository;

@DataJpaTest
public class MedicineRepositoryTest {
  @Autowired
  private MedicineRepository medicineRepository;

  @Autowired
  private AdverseReactionRepository adverseReactionRepository;

  @Autowired
  private ManufacturerRepository manufacturerRepository;

  @BeforeEach
  void cleanUp() {
    medicineRepository.deleteAll();
    adverseReactionRepository.deleteAll();
    manufacturerRepository.deleteAll();
  }

  @Test
  @DisplayName("existsByAdverseReactionsId() should return true when a medicine has adverse reaction id")
  void existsByAdverseReactionsIdShouldReturnTrueWhenAMedicineHasAdverseReactionId() {
    // given
    AdverseReaction adverseReaction = adverseReactionRepository.save(
        new AdverseReaction("a common reaction"));
    Manufacturer manufacturer = manufacturerRepository.save(
        new Manufacturer("manufacturer"));
    Medicine medicine = new Medicine(
        "0.0000.0000.000-0",
        "medicine",
        LocalDate.now(),
        "(00)0000-0000",
        BigDecimal.valueOf(1),
        1,
        manufacturer,
        Set.of(adverseReaction));
    medicineRepository.save(medicine);

    // when
    boolean someMedicineHasAdverseReactionId = medicineRepository.existsByAdverseReactionsId(
        adverseReaction.getId());

    // then
    assertThat(someMedicineHasAdverseReactionId).isTrue();
  }
}
