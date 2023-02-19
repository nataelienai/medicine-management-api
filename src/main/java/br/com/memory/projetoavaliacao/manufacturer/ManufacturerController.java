package br.com.memory.projetoavaliacao.manufacturer;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/manufacturers")
@Tag(name = "Manufacturer", description = "The manufacturer resource API")
public class ManufacturerController {
  private final ManufacturerService manufacturerService;

  @Operation(
    summary = "Get all manufacturers",
    description = "Gets a paged list of manufacturers and filtered by name if provided.",
    responses = {
      @ApiResponse(responseCode = "200", description = "Operation succeeded")
    }
  )
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Page<Manufacturer> findAllBy(
      @Parameter(description = "The name to filter by") @RequestParam(required = false) String name,
      @ParameterObject @PageableDefault Pageable pageable) {
    return manufacturerService.findAllBy(name, pageable);
  }
}
