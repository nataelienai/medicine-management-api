package br.com.memory.projetoavaliacao.medicine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, String> {
  public boolean existsByAdverseReactionsId(Long adverseReactionId);

  @Query("FROM Medicine m WHERE (:registrationNumber IS NULL OR m.registrationNumber LIKE %:registrationNumber%) AND (:name IS NULL OR LOWER(m.name) LIKE CONCAT('%', LOWER(:name), '%'))")
  public Page<Medicine> findAllBy(String registrationNumber, String name, Pageable pageable);
}
