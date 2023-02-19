package br.com.memory.projetoavaliacao.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleException(MethodArgumentNotValidException exception) {
    FieldError fieldError = exception.getFieldError();
    int statusCode = HttpStatus.BAD_REQUEST.value();

    if (fieldError == null) {
      return new ErrorResponse(statusCode, "One of the provided fields failed validation");
    }

    return new ErrorResponse(statusCode, fieldError.getDefaultMessage());
  }

}
