package br.com.memory.projetoavaliacao.manufacturer;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class ManufacturerServiceTest {
  @Mock
  private ManufacturerRepository manufacturerRepository;

  private ManufacturerService manufacturerService;

  @BeforeEach
  void setUp() {
    manufacturerService = new ManufacturerService(manufacturerRepository);
  }

  @Test
  @DisplayName("findAllBy() should call repository with the given arguments")
  void findAllByShouldCallRepositoryWithTheGivenArguments() {
    // given
    String nameFilter = "just a name";
    Pageable page = PageRequest.of(0, 1);

    // when
    manufacturerService.findAllBy(nameFilter, page);

    // then
    verify(manufacturerRepository).findAllBy(nameFilter, page);
  }
}
