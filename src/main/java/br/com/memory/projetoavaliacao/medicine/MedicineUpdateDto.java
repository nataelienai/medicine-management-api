package br.com.memory.projetoavaliacao.medicine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import br.com.memory.projetoavaliacao.shared.validation.Extended;
import br.com.memory.projetoavaliacao.shared.validation.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MedicineUpdateDto {
  @NotBlank(message = "The name field must not be blank")
  private String name;

  @NotNull(message = "The expirationDate field must not be null")
  private LocalDate expirationDate;

  @NotBlank(message = "The customerServicePhone field must not be blank")
  @PhoneNumber(message = "The customerServicePhone field has an invalid phone number format", groups = Extended.class)
  private String customerServicePhone;

  @NotNull(message = "The price field must not be null")
  @PositiveOrZero(message = "The price field must not be negative")
  private BigDecimal price;

  @NotNull(message = "The amountOfPills field must not be null")
  @PositiveOrZero(message = "The amountOfPills field must not be negative")
  private Integer amountOfPills;

  @NotNull(message = "The manufacturerId field must not be null")
  private Long manufacturerId;

  @NotEmpty(message = "The adverseReactionIds field must not be null nor empty")
  private Set<Long> adverseReactionIds;
}
