package br.com.memory.projetoavaliacao.adversereaction;

import org.springframework.stereotype.Service;

import br.com.memory.projetoavaliacao.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AdverseReactionService {
  private final AdverseReactionRepository adverseReactionRepository;

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
}
