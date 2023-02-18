package br.com.memory.projetoavaliacao.manufacturer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/manufacturers")
public class ManufacturerController {
  private final ManufacturerService manufacturerService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Page<Manufacturer> findAllBy(
      @RequestParam(required = false) String name,
      @PageableDefault Pageable pageable) {
    return manufacturerService.findAllBy(name, pageable);
  }
}
