package br.com.memory.projetoavaliacao.manufacturer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
  @Query("FROM Manufacturer m WHERE :name IS NULL OR LOWER(m.name) LIKE CONCAT('%', LOWER(:name), '%')")
  public Page<Manufacturer> findAllBy(String name, Pageable pageable);
}
