package br.com.memory.projetoavaliacao.adversereaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdverseReactionRepository extends JpaRepository<AdverseReaction, Long> {
}
