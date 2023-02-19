package br.com.memory.projetoavaliacao.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

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

  @ExceptionHandler(InvalidFormatException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleException(InvalidFormatException exception) {
    Reference pathReference = exception.getPath().get(0);
    int statusCode = HttpStatus.BAD_REQUEST.value();

    if (pathReference == null) {
      return new ErrorResponse(statusCode, "One of the provided fields has an invalid type or format");
    }

    String problematicField = pathReference.getFieldName();
    String message = String.format("The %s field has an invalid type or format", problematicField);
    return new ErrorResponse(statusCode, message);
  }

  @ExceptionHandler(InvalidDateFormatException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleException(InvalidDateFormatException exception) {
    return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleException(ResourceNotFoundException exception) {
    return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
  }
}
