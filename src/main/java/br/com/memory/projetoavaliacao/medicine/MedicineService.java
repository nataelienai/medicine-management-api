package br.com.memory.projetoavaliacao.medicine;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MedicineService {
  private final MedicineRepository medicineRepository;

  public List<Medicine> findAll() {
    return medicineRepository.findAll();
  }
}
