package br.com.memory.projetoavaliacao.adversereaction;

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

import br.com.memory.projetoavaliacao.shared.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/adverse-reactions")
@Tag(name = "Adverse Reaction", description = "The adverse reaction resource API")
public class AdverseReactionController {
  private final AdverseReactionService adverseReactionService;

  @Operation(
    summary = "Get all adverse reactions",
    description = "Get a paged list of adverse reactions and filtered by description if provided.",
    responses = {
      @ApiResponse(responseCode = "200", description = "Operation succeeded")
    }
  )
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Page<AdverseReaction> findAllBy(
      @Parameter(description = "The description to filter by") @RequestParam(required = false) String description,
      @ParameterObject @PageableDefault Pageable pageable) {
    return adverseReactionService.findAllBy(description, pageable);
  }

  @Operation(
    summary = "Create adverse reaction",
    description = "Create an adverse reaction and returns it.",
    responses = {
      @ApiResponse(responseCode = "201", description = "Operation succeeded"),
      @ApiResponse(
        responseCode = "400",
        description = "Invalid request body",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    }
  )
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AdverseReaction create(@Valid @RequestBody AdverseReactionDto adverseReactionDto) {
    return adverseReactionService.create(adverseReactionDto);
  }

  @Operation(
    summary = "Update adverse reaction",
    description = "Update an adverse reaction by its id and returns it.",
    responses = {
      @ApiResponse(responseCode = "200", description = "Operation succeeded"),
      @ApiResponse(
        responseCode = "400",
        description = "Invalid request body",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
        responseCode = "404",
        description = "Adverse reaction not found",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    }
  )
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public AdverseReaction update(
      @Parameter(description = "Id of the adverse reaction to be updated") @PathVariable Long id,
      @Valid @RequestBody AdverseReactionDto adverseReactionDto) {
    return adverseReactionService.update(id, adverseReactionDto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteById(@PathVariable Long id) {
    adverseReactionService.deleteById(id);
  }
}
