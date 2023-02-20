package br.com.memory.projetoavaliacao.medicine;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/medicines")
@Tag(name = "Medicine", description = "The medicine resource API")
public class MedicineController {
  private final MedicineService medicineService;

  @Operation(
    summary = "Get all medicines",
    description = "Gets a paged list of medicines and filtered by registration number or name if provided.",
    responses = {
      @ApiResponse(responseCode = "200", description = "Operation succeeded")
    }
  )
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Page<Medicine> findAllBy(
      @Parameter(description = "The registration number to filter by") @RequestParam(required = false) String registrationNumber,
      @Parameter(description = "The name to filter by") @RequestParam(required = false) String name,
      @ParameterObject @PageableDefault Pageable pageable) {
    return medicineService.findAllBy(registrationNumber, name, pageable);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Medicine create(@Valid @RequestBody MedicineCreationDto medicineCreationDto) {
    return medicineService.create(medicineCreationDto);
  }

  @PutMapping("/{registrationNumber}")
  @ResponseStatus(HttpStatus.OK)
  public Medicine update(
      @PathVariable String registrationNumber,
      @Valid @RequestBody MedicineUpdateDto medicineUpdateDto) {
    return medicineService.update(registrationNumber, medicineUpdateDto);
  }

  @DeleteMapping("/{registrationNumber}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteByRegistrationNumber(@PathVariable String registrationNumber) {
    medicineService.deleteByRegistrationNumber(registrationNumber);
  }
}
