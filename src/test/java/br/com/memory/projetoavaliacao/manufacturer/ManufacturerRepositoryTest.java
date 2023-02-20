package br.com.memory.projetoavaliacao.manufacturer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;

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
    assertThat(retrievedManufacturers.getTotalElements()).isEqualTo(3);
  }
}
