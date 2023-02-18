package br.com.memory.projetoavaliacao.manufacturer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ManufacturerService {
  private final ManufacturerRepository manufacturerRepository;

  public Page<Manufacturer> findAllBy(String name, Pageable pageable) {
    return manufacturerRepository.findAllBy(name, pageable);
  }
}
