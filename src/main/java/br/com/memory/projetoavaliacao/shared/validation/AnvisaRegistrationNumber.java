package br.com.memory.projetoavaliacao.shared.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = AnvisaRegistrationNumberValidator.class)
@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AnvisaRegistrationNumber {
  String message() default "Invalid ANVISA registration number";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
