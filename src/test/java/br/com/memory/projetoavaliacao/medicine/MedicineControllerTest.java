package br.com.memory.projetoavaliacao.medicine;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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

import br.com.memory.projetoavaliacao.adversereaction.AdverseReaction;
import br.com.memory.projetoavaliacao.manufacturer.Manufacturer;
import br.com.memory.projetoavaliacao.shared.exception.ResourceNotFoundException;

@WebMvcTest(MedicineController.class)
public class MedicineControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private MedicineService medicineService;

  @Test
  @DisplayName("GET /medicines should return 200 and paged list of medicines when given filter and pagination")
  void getMedicinesShouldReturn200AndPagedListOfMedicinesWhenGivenFilterAndPagination() throws Exception {
    // given
    Pageable pageable = PageRequest.of(0, 10);
    List<Medicine> medicines = List.of(makeMedicine());
    Page<Medicine> pagedMedicines = new PageImpl<>(
        medicines,
        pageable,
        medicines.size());

    String registrationNumberFilter = "1.";
    String nameFilter = "med";
    when(medicineService.findAllBy(registrationNumberFilter, nameFilter, pageable))
        .thenReturn(pagedMedicines);

    // when
    // then
    String expected = objectMapper.writeValueAsString(pagedMedicines);
    mockMvc.perform(get("/medicines")
        .param("registrationNumber", registrationNumberFilter)
        .param("name", nameFilter)
        .param("page", "0")
        .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(content().string(expected));
  }

  @Test
  @DisplayName("GET /medicines should return 200 and paged list of medicines when not given filter and pagination")
  void getMedicinesShouldReturn200AndPagedListOfMedicinesWhenNotGivenFilterAndPagination() throws Exception {
    // given
    Pageable pageable = PageRequest.of(0, 10);
    List<Medicine> medicines = List.of(makeMedicine());
    Page<Medicine> pagedMedicines = new PageImpl<>(
        medicines,
        pageable,
        medicines.size());

    when(medicineService.findAllBy(eq(null), eq(null), any(Pageable.class)))
        .thenReturn(pagedMedicines);

    // when
    // then
    String expected = objectMapper.writeValueAsString(pagedMedicines);
    mockMvc.perform(get("/medicines"))
        .andExpect(status().isOk())
        .andExpect(content().string(expected));
  }

  @Test
  @DisplayName("DELETE /medicines/{registrationNumber} should return 204 when given valid registration number")
  void deleteMedicineShouldReturn204WhenGivenValidRegistrationNumber() throws Exception {
    // given
    String registrationNumber = "1.4444.4444.333-1";
    doThrow(ResourceNotFoundException.class)
        .when(medicineService)
        .deleteByRegistrationNumber(any());
    doNothing()
        .when(medicineService)
        .deleteByRegistrationNumber(registrationNumber);

    // when
    // then
    mockMvc.perform(delete("/medicines/{registrationNumber}", registrationNumber))
        .andExpect(status().isNoContent());
  }

  private Medicine makeMedicine() {
    return new Medicine(
        "1.4444.4444.333-1",
        "medicine",
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        1,
        new Manufacturer(1L, "Manufacturer"),
        Set.of(new AdverseReaction(1L, "Strong reaction")));
  }
}
