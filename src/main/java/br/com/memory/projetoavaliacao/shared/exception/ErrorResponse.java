package br.com.memory.projetoavaliacao.shared.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
  private int statusCode;
  private String message;
}
