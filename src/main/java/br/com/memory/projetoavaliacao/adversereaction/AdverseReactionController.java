package br.com.memory.projetoavaliacao.adversereaction;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/adverse-reactions")
public class AdverseReactionController {
  private final AdverseReactionService adverseReactionService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Page<AdverseReaction> findAllBy(
      @RequestParam(required = false) String description,
      @PageableDefault Pageable pageable) {
    return adverseReactionService.findAllBy(description, pageable);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AdverseReaction create(@Valid @RequestBody AdverseReactionDto adverseReactionDto) {
    return adverseReactionService.create(adverseReactionDto);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public AdverseReaction update(@PathVariable Long id, @Valid @RequestBody AdverseReactionDto adverseReactionDto) {
    return adverseReactionService.update(id, adverseReactionDto);
  }
}
