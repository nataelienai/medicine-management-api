package br.com.memory.projetoavaliacao.adversereaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdverseReactionRepository extends JpaRepository<AdverseReaction, Long> {
  @Query("FROM AdverseReaction ar WHERE :description IS NULL OR LOWER(ar.description) LIKE CONCAT('%', LOWER(:description), '%')")
  public Page<AdverseReaction> findAllBy(String description, Pageable pageable);
}
