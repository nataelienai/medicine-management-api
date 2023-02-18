package br.com.memory.projetoavaliacao.adversereaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.memory.projetoavaliacao.medicine.MedicineRepository;
import br.com.memory.projetoavaliacao.shared.exception.ResourceLinkedToAnotherException;
import br.com.memory.projetoavaliacao.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AdverseReactionService {
  private final AdverseReactionRepository adverseReactionRepository;
  private final MedicineRepository medicineRepository;

  public Page<AdverseReaction> findAllBy(String description, Pageable pageable) {
    return adverseReactionRepository.findAllBy(description, pageable);
  }

  public AdverseReaction create(AdverseReactionDto adverseReactionDto) {
    AdverseReaction adverseReaction = new AdverseReaction(
        adverseReactionDto.getDescription());

    return adverseReactionRepository.save(adverseReaction);
  }

  public AdverseReaction update(Long id, AdverseReactionDto adverseReactionDto) {
    AdverseReaction adverseReaction = adverseReactionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format("Adverse reaction with id %s not found", id)));

    adverseReaction.setDescription(adverseReactionDto.getDescription());

    return adverseReactionRepository.save(adverseReaction);
  }

  public void deleteById(Long id) {
    boolean idExists = adverseReactionRepository.existsById(id);

    if (!idExists) {
      throw new ResourceNotFoundException(
          String.format("Adverse reaction with id %s not found", id));
    }

    boolean isLinkedToMedicines = medicineRepository.existsByAdverseReactionsId(id);

    if (isLinkedToMedicines) {
      throw new ResourceLinkedToAnotherException(
          String.format("Adverse reaction with id %s is linked to a medicine", id));
    }

    adverseReactionRepository.deleteById(id);
  }
}
