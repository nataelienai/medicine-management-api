package br.com.memory.projetoavaliacao.manufacturer;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ManufacturerService {
  private final ManufacturerRepository manufacturerRepository;

  public List<Manufacturer> findAll() {
    return manufacturerRepository.findAll();
  }
}
