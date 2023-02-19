package br.com.memory.projetoavaliacao.medicine;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/medicines")
public class MedicineController {
  private final MedicineService medicineService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Page<Medicine> findAllBy(
      @RequestParam(required = false) String registrationNumber,
      @RequestParam(required = false) String name,
      @PageableDefault Pageable pageable) {
    return medicineService.findAllBy(registrationNumber, name, pageable);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Medicine create(@Valid @RequestBody MedicineCreationDto medicineCreationDto) {
    return medicineService.create(medicineCreationDto);
  }
}
