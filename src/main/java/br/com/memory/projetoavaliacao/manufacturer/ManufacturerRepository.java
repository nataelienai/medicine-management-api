package br.com.memory.projetoavaliacao.manufacturer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
  @Query(value = "SELECT * FROM manufacturer m WHERE m.name ILIKE %:name%", nativeQuery = true)
  public Page<Manufacturer> findAllBy(String name, Pageable pageable);
}
