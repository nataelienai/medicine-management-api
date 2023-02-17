package br.com.memory.projetoavaliacao.medicine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MedicineUpdateDto {
  private String name;
  private LocalDate expirationDate;
  private String customerServicePhone;
  private BigDecimal price;
  private Integer amountOfPills;
  private Long manufacturerId;
  private Set<Long> adverseReactionIds;
}
