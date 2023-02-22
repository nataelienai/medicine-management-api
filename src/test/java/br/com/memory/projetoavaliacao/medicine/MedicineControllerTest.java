package br.com.memory.projetoavaliacao.medicine;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.memory.projetoavaliacao.adversereaction.AdverseReaction;
import br.com.memory.projetoavaliacao.manufacturer.Manufacturer;
import br.com.memory.projetoavaliacao.shared.exception.ErrorResponse;
import br.com.memory.projetoavaliacao.shared.exception.ResourceAlreadyExistsException;
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
    List<Medicine> medicines = List.of(makeMedicine(1L, 1L));
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
    List<Medicine> medicines = List.of(makeMedicine(1L, 1L));
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
  @DisplayName("POST /medicines should return 201 and medicine when given valid input")
  void postMedicinesShouldReturn201AndMedicineWhenGivenValidInput() throws Exception {
    // given
    Long manufacturerId = 1L;
    Long adverseReactionId = 1L;
    Medicine medicine = makeMedicine(manufacturerId, adverseReactionId);
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        medicine.getRegistrationNumber(),
        medicine.getName(),
        medicine.getExpirationDate(),
        medicine.getCustomerServicePhone(),
        medicine.getPrice(),
        medicine.getAmountOfPills(),
        manufacturerId,
        Set.of(adverseReactionId));
    when(medicineService.create(medicineCreationDto)).thenReturn(medicine);
    String serializedMedicineDto = objectMapper.writeValueAsString(medicineCreationDto);

    // when
    // then
    String expected = objectMapper.writeValueAsString(medicine);
    mockMvc.perform(post("/medicines")
        .contentType(MediaType.APPLICATION_JSON)
        .content(serializedMedicineDto))
        .andExpect(status().isCreated())
        .andExpect(content().string(expected));
  }

  @Test
  @DisplayName("POST /medicines should return 400 when given null registration number")
  void postMedicinesShouldReturn400WhenGivenNullRegistrationNumber() throws Exception {
    // given
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        null,
        "medicine",
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        1,
        1L,
        Set.of(1L));
    // when
    // then
    assertThatPostMethodReturns400(medicineCreationDto, "The registrationNumber field must not be blank");
  }

  @Test
  @DisplayName("POST /medicines should return 400 when given an empty registration number")
  void postMedicinesShouldReturn400WhenGivenEmptyRegistrationNumber() throws Exception {
    // given
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "",
        "medicine",
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        1,
        1L,
        Set.of(1L));
    // when
    // then
    assertThatPostMethodReturns400(medicineCreationDto, "The registrationNumber field must not be blank");
  }

  @Test
  @DisplayName("POST /medicines should return 400 when given an invalid anvisa registration number")
  void postMedicinesShouldReturn400WhenGivenInvalidAnvisaRegistrationNumber() throws Exception {
    // given
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "1.333.4444",
        "medicine",
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        1,
        1L,
        Set.of(1L));
    // when
    // then
    assertThatPostMethodReturns400(
        medicineCreationDto,
        "The registrationNumber field has an invalid ANVISA registration number");
  }

  @Test
  @DisplayName("POST /medicines should return 400 when given null name")
  void postMedicinesShouldReturn400WhenGivenNullName() throws Exception {
    // given
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "1.4444.4444.333-1",
        null,
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        1,
        1L,
        Set.of(1L));
    // when
    // then
    assertThatPostMethodReturns400(medicineCreationDto, "The name field must not be blank");
  }

  @Test
  @DisplayName("POST /medicines should return 400 when given an empty name")
  void postMedicinesShouldReturn400WhenGivenEmptyName() throws Exception {
    // given
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "1.4444.4444.333-1",
        "",
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        1,
        1L,
        Set.of(1L));
    // when
    // then
    assertThatPostMethodReturns400(medicineCreationDto, "The name field must not be blank");
  }

  @Test
  @DisplayName("POST /medicines should return 400 when given null expiration date")
  void postMedicinesShouldReturn400WhenGivenNullExpirationDate() throws Exception {
    // given
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "1.4444.4444.333-1",
        "medicine",
        null,
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        1,
        1L,
        Set.of(1L));
    // when
    // then
    assertThatPostMethodReturns400(medicineCreationDto, "The expirationDate field must not be null");
  }

  @Test
  @DisplayName("POST /medicines should return 400 when given null customer service phone")
  void postMedicinesShouldReturn400WhenGivenNullCustomerServicePhone() throws Exception {
    // given
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "1.4444.4444.333-1",
        "medicine",
        LocalDate.now(),
        null,
        BigDecimal.valueOf(1),
        1,
        1L,
        Set.of(1L));
    // when
    // then
    assertThatPostMethodReturns400(medicineCreationDto, "The customerServicePhone field must not be blank");
  }

  @Test
  @DisplayName("POST /medicines should return 400 when given an empty customer service phone")
  void postMedicinesShouldReturn400WhenGivenEmptyCustomerServicePhone() throws Exception {
    // given
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "1.4444.4444.333-1",
        "medicine",
        LocalDate.now(),
        "",
        BigDecimal.valueOf(1),
        1,
        1L,
        Set.of(1L));
    // when
    // then
    assertThatPostMethodReturns400(medicineCreationDto, "The customerServicePhone field must not be blank");
  }

  @Test
  @DisplayName("POST /medicines should return 400 when given an invalid customer service phone")
  void postMedicinesShouldReturn400WhenGivenInvalidCustomerServicePhone() throws Exception {
    // given
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "1.4444.4444.333-1",
        "medicine",
        LocalDate.now(),
        "121234",
        BigDecimal.valueOf(1),
        1,
        1L,
        Set.of(1L));
    // when
    // then
    assertThatPostMethodReturns400(
        medicineCreationDto,
        "The customerServicePhone field has an invalid phone number format");
  }

  @Test
  @DisplayName("POST /medicines should return 400 when given a null price")
  void postMedicinesShouldReturn400WhenGivenNullPrice() throws Exception {
    // given
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "1.4444.4444.333-1",
        "medicine",
        LocalDate.now(),
        "(12)0000-0000",
        null,
        1,
        1L,
        Set.of(1L));
    // when
    // then
    assertThatPostMethodReturns400(
        medicineCreationDto,
        "The price field must not be null");
  }

  @Test
  @DisplayName("POST /medicines should return 400 when given a negative price")
  void postMedicinesShouldReturn400WhenGivenNegativePrice() throws Exception {
    // given
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "1.4444.4444.333-1",
        "medicine",
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(-1),
        1,
        1L,
        Set.of(1L));
    // when
    // then
    assertThatPostMethodReturns400(
        medicineCreationDto,
        "The price field must not be negative");
  }

  @Test
  @DisplayName("POST /medicines should return 400 when given a null amount of pills")
  void postMedicinesShouldReturn400WhenGivenNullAmountOfPills() throws Exception {
    // given
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "1.4444.4444.333-1",
        "medicine",
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        null,
        1L,
        Set.of(1L));
    // when
    // then
    assertThatPostMethodReturns400(
        medicineCreationDto,
        "The amountOfPills field must not be null");
  }

  @Test
  @DisplayName("POST /medicines should return 400 when given a negative amount of pills")
  void postMedicinesShouldReturn400WhenGivenNegativeAmountOfPills() throws Exception {
    // given
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "1.4444.4444.333-1",
        "medicine",
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        -1,
        1L,
        Set.of(1L));
    // when
    // then
    assertThatPostMethodReturns400(
        medicineCreationDto,
        "The amountOfPills field must not be negative");
  }

  @Test
  @DisplayName("POST /medicines should return 400 when given a null manufacturer id")
  void postMedicinesShouldReturn400WhenGivenNullManufacturerId() throws Exception {
    // given
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "1.4444.4444.333-1",
        "medicine",
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        1,
        null,
        Set.of(1L));
    // when
    // then
    assertThatPostMethodReturns400(
        medicineCreationDto,
        "The manufacturerId field must not be null");
  }

  @Test
  @DisplayName("POST /medicines should return 400 when given a null adverse reaction ids")
  void postMedicinesShouldReturn400WhenGivenNullAdverseReactionIds() throws Exception {
    // given
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "1.4444.4444.333-1",
        "medicine",
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        1,
        1L,
        null);
    // when
    // then
    assertThatPostMethodReturns400(
        medicineCreationDto,
        "The adverseReactionIds field must not be null nor empty");
  }

  @Test
  @DisplayName("POST /medicines should return 400 when given an empty set of adverse reaction ids")
  void postMedicinesShouldReturn400WhenGivenEmptyAdverseReactionIds() throws Exception {
    // given
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "1.4444.4444.333-1",
        "medicine",
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        1,
        1L,
        Set.of());
    // when
    // then
    assertThatPostMethodReturns400(
        medicineCreationDto,
        "The adverseReactionIds field must not be null nor empty");
  }

  @Test
  @DisplayName("POST /medicines should return 404 when manufacturer or adverse reaction id was not found")
  void postMedicinesShouldReturn400WhenGivenManufacturerOrAdverseReactionIdNotFound() throws Exception {
    // given
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "1.4444.4444.333-1",
        "medicine",
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        1,
        1L,
        Set.of(1L));

    when(medicineService.create(medicineCreationDto))
        .thenThrow(ResourceNotFoundException.class);

    String serializedMedicineDto = objectMapper.writeValueAsString(medicineCreationDto);

    // when
    // then
    mockMvc.perform(post("/medicines")
        .contentType(MediaType.APPLICATION_JSON)
        .content(serializedMedicineDto))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("POST /medicines should return 409 when registration number already exists")
  void postMedicinesShouldReturn400WhenGivenRegistrationNumberAlreadyExists() throws Exception {
    // given
    MedicineCreationDto medicineCreationDto = new MedicineCreationDto(
        "1.4444.4444.333-1",
        "medicine",
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        1,
        1L,
        Set.of(1L));

    when(medicineService.create(medicineCreationDto))
        .thenThrow(ResourceAlreadyExistsException.class);

    String serializedMedicineDto = objectMapper.writeValueAsString(medicineCreationDto);

    // when
    // then
    mockMvc.perform(post("/medicines")
        .contentType(MediaType.APPLICATION_JSON)
        .content(serializedMedicineDto))
        .andExpect(status().isConflict());
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

  @Test
  @DisplayName("DELETE /medicines/{registrationNumber} should return 404 when given non-existent registration number")
  void deleteMedicineShouldReturn404WhenGivenNonExistentRegistrationNumber() throws Exception {
    // given
    String registrationNumber = "1.4444.4444.333-1";
    doThrow(ResourceNotFoundException.class)
        .when(medicineService)
        .deleteByRegistrationNumber(registrationNumber);

    // when
    // then
    mockMvc.perform(delete("/medicines/{registrationNumber}", registrationNumber))
        .andExpect(status().isNotFound());
  }

  private Medicine makeMedicine(Long manufacturerId, Long adverseReactionId) {
    return new Medicine(
        "1.4444.4444.333-1",
        "medicine",
        LocalDate.now(),
        "(12)0000-0000",
        BigDecimal.valueOf(1),
        1,
        new Manufacturer(manufacturerId, "Manufacturer"),
        Set.of(new AdverseReaction(adverseReactionId, "Strong reaction")));
  }

  private void assertThatPostMethodReturns400(MedicineCreationDto medicineDto, String expectedMessage)
      throws Exception {
    String serializedMedicineDto = objectMapper.writeValueAsString(medicineDto);

    ErrorResponse errorResponse = new ErrorResponse(400, expectedMessage);
    String expected = objectMapper.writeValueAsString(errorResponse);

    mockMvc.perform(post("/medicines")
        .contentType(MediaType.APPLICATION_JSON)
        .content(serializedMedicineDto))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(expected));
  }
}
