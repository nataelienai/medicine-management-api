package br.com.memory.projetoavaliacao;

import java.time.format.DateTimeFormatter;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import br.com.memory.projetoavaliacao.shared.mapping.LocalDateDeserializer;

@Configuration
public class ApplicationConfig {
  private static final String dateFormat = "dd/MM/yyyy";

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    return builder -> {
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
      builder.serializers(new LocalDateSerializer(dtf));
      builder.deserializers(new LocalDateDeserializer(dtf));
    };
  }
}
