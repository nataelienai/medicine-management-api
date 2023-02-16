package br.com.memory.projetoavaliacao.adversereaction;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AdverseReactionService {
  private final AdverseReactionRepository adverseReactionRepository;

  public AdverseReaction create(AdverseReactionDto adverseReactionDto) {
    AdverseReaction adverseReaction = new AdverseReaction(adverseReactionDto.getDescription());
    return adverseReactionRepository.save(adverseReaction);
  }
}
