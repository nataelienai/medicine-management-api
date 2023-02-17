package br.com.memory.projetoavaliacao.medicine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import br.com.memory.projetoavaliacao.adversereaction.AdverseReaction;
import br.com.memory.projetoavaliacao.manufacturer.Manufacturer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Medicine {
  @Id
  private String registrationNumber;
  private String name;
  private LocalDate expirationDate;
  private String customerServicePhone;
  private BigDecimal price;
  private Integer amountOfPills;

  @ManyToOne
  private Manufacturer manufacturer;

  @ManyToMany
  @JoinTable(
    name = "medicine_adverse_reaction",
    joinColumns = @JoinColumn(name = "medicine_registration_number"),
    inverseJoinColumns = @JoinColumn(name = "adverse_reaction_id"))
  private Set<AdverseReaction> adverseReactions;
}
