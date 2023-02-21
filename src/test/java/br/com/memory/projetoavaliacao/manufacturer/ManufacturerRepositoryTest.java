package br.com.memory.projetoavaliacao.manufacturer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
public class ManufacturerRepositoryTest {
  @Autowired
  private ManufacturerRepository manufacturerRepository;

  @BeforeEach
  void cleanUp() {
    manufacturerRepository.deleteAll();
  }

  @Test
  @DisplayName("findAllBy() should return all manufacturers when not given name and pagination")
  void findAllByShouldReturnAllManufacturersWhenNotGivenNameAndPagination() {
    // given
    Set<Manufacturer> manufacturers = Set.of(
        new Manufacturer("Manufacturer 1"),
        new Manufacturer("Manufacturer 2"),
        new Manufacturer("Manufacturer 3"));
    manufacturerRepository.saveAll(manufacturers);

    // when
    Page<Manufacturer> retrievedManufacturers = manufacturerRepository.findAllBy(null, null);

    // then
    assertThat(retrievedManufacturers.getTotalElements()).isEqualTo(manufacturers.size());
    assertThat(retrievedManufacturers.getNumberOfElements()).isEqualTo(manufacturers.size());
  }

  @Test
  @DisplayName("findAllBy() should return a paged list of manufacturers when given only pagination")
  void findAllByShouldReturnPagedListOfManufacturersWhenGivenOnlyPagination() {
    // given
    Set<Manufacturer> manufacturers = Set.of(
        new Manufacturer("Manufacturer 1"),
        new Manufacturer("Manufacturer 2"),
        new Manufacturer("Manufacturer 3"));
    manufacturerRepository.saveAll(manufacturers);

    // when
    Pageable firstPage = PageRequest.of(0, 2);
    Page<Manufacturer> pagedManufacturers = manufacturerRepository.findAllBy(null, firstPage);

    // then
    assertThat(pagedManufacturers.getTotalElements()).isEqualTo(manufacturers.size());
    assertThat(pagedManufacturers.getNumberOfElements()).isEqualTo(firstPage.getPageSize());
    assertThat(pagedManufacturers.getSize()).isEqualTo(firstPage.getPageSize());
    assertThat(pagedManufacturers.getNumber()).isEqualTo(firstPage.getPageNumber());
  }

  @Test
  @DisplayName("findAllBy() should return a filtered list of manufacturers when given only name")
  void findAllByShouldReturnFilteredListOfManufacturersWhenGivenOnlyName() {
    // given
    List<String> manufacturerNames = List.of("The Manu", "The Facturer", "The Manufacturer");
    Set<Manufacturer> manufacturers = manufacturerNames.stream()
        .map(name -> new Manufacturer(name))
        .collect(Collectors.toSet());
    manufacturerRepository.saveAll(manufacturers);

    // when
    String nameFilter = "manu";
    Page<Manufacturer> filteredManufacturers = manufacturerRepository.findAllBy(nameFilter, null);

    // then
    List<String> expectedManufacturerNames = List.of("The Manu", "The Manufacturer");
    assertThat(filteredManufacturers.getTotalElements()).isEqualTo(expectedManufacturerNames.size());
    assertThat(filteredManufacturers.getNumberOfElements()).isEqualTo(expectedManufacturerNames.size());
    filteredManufacturers.getContent().forEach(manufacturer -> {
      assertThat(manufacturer.getName()).isIn(expectedManufacturerNames);
    });
  }

  @Test
  @DisplayName("findAllBy() should return a filtered and paged list of manufacturers when given name and pagination")
  void findAllByShouldReturnFilteredAndPagedListOfManufacturersWhenGivenNameAndPagination() {
    // given
    List<String> manufacturerNames = List.of("The Manu", "The Facturer", "The Manufacturer", "A Manufacturer");
    Set<Manufacturer> manufacturers = manufacturerNames.stream()
        .map(name -> new Manufacturer(name))
        .collect(Collectors.toSet());
    manufacturerRepository.saveAll(manufacturers);

    // when
    String nameFilter = "facturer";
    Pageable firstPage = PageRequest.of(0, 2);
    Page<Manufacturer> filteredAndPagedManufacturers = manufacturerRepository.findAllBy(nameFilter, firstPage);

    // then
    List<String> expectedManufacturerNames = List.of("The Facturer", "The Manufacturer", "A Manufacturer");
    assertThat(filteredAndPagedManufacturers.getTotalElements()).isEqualTo(expectedManufacturerNames.size());
    assertThat(filteredAndPagedManufacturers.getNumberOfElements()).isEqualTo(firstPage.getPageSize());
    assertThat(filteredAndPagedManufacturers.getSize()).isEqualTo(firstPage.getPageSize());
    assertThat(filteredAndPagedManufacturers.getNumber()).isEqualTo(firstPage.getPageNumber());
    filteredAndPagedManufacturers.getContent().forEach(manufacturer -> {
      assertThat(manufacturer.getName()).isIn(expectedManufacturerNames);
    });
  }
}
