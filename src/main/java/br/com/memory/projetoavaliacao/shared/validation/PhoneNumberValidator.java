package br.com.memory.projetoavaliacao.shared.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return Pattern.matches("^\\(\\d{2}\\)\\d{4}-\\d{4}$", value);
  }
}
