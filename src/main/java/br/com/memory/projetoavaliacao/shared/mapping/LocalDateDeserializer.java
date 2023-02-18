package br.com.memory.projetoavaliacao.shared.mapping;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import br.com.memory.projetoavaliacao.shared.exception.InvalidDateFormatException;

public class LocalDateDeserializer extends StdDeserializer<LocalDate> {
  private final DateTimeFormatter dtf;

  public LocalDateDeserializer(DateTimeFormatter dtf) {
    super(LocalDate.class);
    this.dtf = dtf;
  }

  @Override
  public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
    String value = p.readValueAs(String.class);

    if (value == null) {
      return null;
    }

    try {
      return LocalDate.from(dtf.parse(value));
    } catch (DateTimeParseException e) {
      throw new InvalidDateFormatException(p.currentName() + " has an invalid date format");
    }
  }
}
