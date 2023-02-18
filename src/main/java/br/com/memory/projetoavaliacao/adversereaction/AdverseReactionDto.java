package br.com.memory.projetoavaliacao.adversereaction;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdverseReactionDto {
  @NotBlank(message = "description must not be blank")
  private String description;
}
