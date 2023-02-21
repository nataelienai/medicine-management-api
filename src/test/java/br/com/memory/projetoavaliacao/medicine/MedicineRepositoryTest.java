package br.com.memory.projetoavaliacao.medicine;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

  @Test
  @DisplayName("existsByAdverseReactionsId() should return false when no medicine has adverse reaction id")
  void existsByAdverseReactionsIdShouldReturnFalseWhenNoMedicineHasAdverseReactionId() {
    // given
    AdverseReaction adverseReaction = adverseReactionRepository.save(
        new AdverseReaction("a common reaction"));

    // when
    boolean someMedicineHasAdverseReactionId = medicineRepository.existsByAdverseReactionsId(
        adverseReaction.getId());

    // then
    assertThat(someMedicineHasAdverseReactionId).isFalse();
  }

  @Test
  @DisplayName("findAllBy() should return all medicines when not given registration number, name and pagination")
  void findAllByShouldReturnAllMedicinesWhenNotGivenRegistrationNumberAndNameAndPagination() {
    // given
    Manufacturer manufacturer = manufacturerRepository.save(
        new Manufacturer("manufacturer"));
    Set<Medicine> medicines = Set.of(
        new Medicine(
            "0.0000.0000.000-0",
            "medicine 1",
            LocalDate.now(),
            "(00)0000-0000",
            BigDecimal.valueOf(1),
            1,
            manufacturer,
            Set.of()),
        new Medicine(
            "1.0000.0000.000-0",
            "medicine 2",
            LocalDate.now(),
            "(00)0000-0000",
            BigDecimal.valueOf(1),
            1,
            manufacturer,
            Set.of()));
    medicineRepository.saveAll(medicines);

    // when
    Page<Medicine> fetchedMedicines = medicineRepository.findAllBy(null, null, null);

    // then
    assertThat(fetchedMedicines.getTotalElements()).isEqualTo(medicines.size());
    assertThat(fetchedMedicines.getNumberOfElements()).isEqualTo(medicines.size());
  }

  @Test
  @DisplayName("findAllBy() should return a paged list of medicines when given only pagination")
  void findAllByShouldReturnPagedListOfMedicinesWhenGivenOnlyPagination() {
    // given
    Manufacturer manufacturer = manufacturerRepository.save(
        new Manufacturer("manufacturer"));
    Set<Medicine> medicines = Set.of(
        new Medicine(
            "0.0000.0000.000-0",
            "medicine 1",
            LocalDate.now(),
            "(00)0000-0000",
            BigDecimal.valueOf(1),
            1,
            manufacturer,
            Set.of()),
        new Medicine(
            "1.0000.0000.000-0",
            "medicine 2",
            LocalDate.now(),
            "(00)0000-0000",
            BigDecimal.valueOf(1),
            1,
            manufacturer,
            Set.of()));
    medicineRepository.saveAll(medicines);

    // when
    Pageable firstPage = PageRequest.of(0, 1);
    Page<Medicine> pagedMedicines = medicineRepository.findAllBy(null, null, firstPage);

    // then
    assertThat(pagedMedicines.getTotalElements()).isEqualTo(medicines.size());
    assertThat(pagedMedicines.getNumberOfElements()).isEqualTo(firstPage.getPageSize());
    assertThat(pagedMedicines.getSize()).isEqualTo(firstPage.getPageSize());
    assertThat(pagedMedicines.getNumber()).isEqualTo(firstPage.getPageNumber());
  }

  @Test
  @DisplayName("findAllBy() should return a filtered list of medicines when given only registration number")
  void findAllByShouldReturnFilteredListOfMedicinesWhenGivenOnlyRegistrationNumber() {
    // given
    Manufacturer manufacturer = manufacturerRepository.save(
        new Manufacturer("manufacturer"));
    Set<Medicine> medicines = Set.of(
        new Medicine(
            "0.0000.0000.000-0",
            "medicine 1",
            LocalDate.now(),
            "(00)0000-0000",
            BigDecimal.valueOf(1),
            1,
            manufacturer,
            Set.of()),
        new Medicine(
            "1.0000.0000.000-0",
            "medicine 2",
            LocalDate.now(),
            "(00)0000-0000",
            BigDecimal.valueOf(1),
            1,
            manufacturer,
            Set.of()));
    medicineRepository.saveAll(medicines);

    // when
    String registrationNumberFilter = "0.0000.0000.000-0";
    Page<Medicine> filteredMedicines = medicineRepository.findAllBy(registrationNumberFilter, null, null);

    // then
    List<String> expectedRegistrationNumbers = List.of(registrationNumberFilter);
    assertThat(filteredMedicines.getTotalElements()).isEqualTo(expectedRegistrationNumbers.size());
    assertThat(filteredMedicines.getNumberOfElements()).isEqualTo(expectedRegistrationNumbers.size());
    filteredMedicines.getContent().forEach(medicine -> {
      assertThat(medicine.getRegistrationNumber()).isIn(expectedRegistrationNumbers);
    });
  }
}
