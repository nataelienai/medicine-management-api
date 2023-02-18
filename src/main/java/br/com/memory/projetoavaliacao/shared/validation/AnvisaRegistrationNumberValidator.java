package br.com.memory.projetoavaliacao.shared.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AnvisaRegistrationNumberValidator
    implements ConstraintValidator<AnvisaRegistrationNumber, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return false;
    }
    return Pattern.matches("^\\d\\.\\d{4}\\.\\d{4}\\.\\d{3}-\\d$", value);
  }
}
