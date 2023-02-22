package br.com.memory.projetoavaliacao.manufacturer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ManufacturerController.class)
public class ManufacturerControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ManufacturerService manufacturerService;

  @Test
  @DisplayName("GET /manufacturers should return 200 and paged list of manufacturers when given filter and pagination")
  void getManufacturersShouldReturn200AndPagedListOfManufacturersWhenGivenFilterAndPagination() throws Exception {
    // given
    Pageable pageable = PageRequest.of(0, 10);
    List<Manufacturer> manufacturers = List.of(new Manufacturer(1L, "Manufacturer"));
    Page<Manufacturer> pagedManufacturers = new PageImpl<>(manufacturers, pageable, manufacturers.size());

    when(manufacturerService.findAllBy("manu", pageable))
        .thenReturn(pagedManufacturers);

    // when
    // then
    String expected = objectMapper.writeValueAsString(pagedManufacturers);
    mockMvc.perform(get("/manufacturers")
        .param("name", "manu")
        .param("page", "0")
        .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(content().string(expected));
  }

  @Test
  @DisplayName("GET /manufacturers should return 200 and paged list of manufacturers when not given filter and pagination")
  void getManufacturersShouldReturn200AndPagedListOfManufacturersWhenNotGivenFilterAndPagination() throws Exception {
    // given
    Pageable pageable = PageRequest.of(0, 10);
    List<Manufacturer> manufacturers = List.of(new Manufacturer(1L, "Manufacturer"));
    Page<Manufacturer> pagedManufacturers = new PageImpl<>(manufacturers, pageable, manufacturers.size());

    when(manufacturerService.findAllBy(eq(null), any(Pageable.class)))
        .thenReturn(pagedManufacturers);

    // when
    // then
    String expected = objectMapper.writeValueAsString(pagedManufacturers);
    mockMvc.perform(get("/manufacturers"))
        .andExpect(status().isOk())
        .andExpect(content().string(expected));
  }
}
