package br.com.memory.projetoavaliacao.medicine;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.memory.projetoavaliacao.adversereaction.AdverseReaction;
import br.com.memory.projetoavaliacao.adversereaction.AdverseReactionRepository;
import br.com.memory.projetoavaliacao.manufacturer.Manufacturer;
import br.com.memory.projetoavaliacao.manufacturer.ManufacturerRepository;
import br.com.memory.projetoavaliacao.shared.exception.ResourceAlreadyExistsException;
import br.com.memory.projetoavaliacao.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MedicineService {
  private final MedicineRepository medicineRepository;
  private final ManufacturerRepository manufacturerRepository;
  private final AdverseReactionRepository adverseReactionRepository;

  public Page<Medicine> findAllBy(String registrationNumber, String name, Pageable pageable) {
    return medicineRepository.findAllBy(registrationNumber, name, pageable);
  }

  public Medicine create(MedicineCreationDto medicineCreationDto) {
    ensureRegistrationNumberIsUnique(medicineCreationDto.getRegistrationNumber());
    Manufacturer manufacturer = findManufacturerById(medicineCreationDto.getManufacturerId());
    Set<AdverseReaction> adverseReactions = findAdverseReactionsByIds(medicineCreationDto.getAdverseReactionIds());

    Medicine medicine = new Medicine(
        medicineCreationDto.getRegistrationNumber(),
        medicineCreationDto.getName(),
        medicineCreationDto.getExpirationDate(),
        medicineCreationDto.getCustomerServicePhone(),
        medicineCreationDto.getPrice(),
        medicineCreationDto.getAmountOfPills(),
        manufacturer,
        adverseReactions);

    return medicineRepository.save(medicine);
  }

  private void ensureRegistrationNumberIsUnique(String registrationNumber) {
    boolean registrationNumberExists = medicineRepository.existsById(registrationNumber);

    if (registrationNumberExists) {
      throw new ResourceAlreadyExistsException(
          String.format("Medicine with registration number %s already exists", registrationNumber));
    }
  }

  private Manufacturer findManufacturerById(Long manufacturerId) {
    return manufacturerRepository.findById(manufacturerId)
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format("Manufacturer with id %s not found", manufacturerId)));
  }

  private Set<AdverseReaction> findAdverseReactionsByIds(Set<Long> adverseReactionIds) {
    List<AdverseReaction> adverseReactions = adverseReactionRepository.findAllById(adverseReactionIds);

    int numberOfAdverseReactionsNotFound = adverseReactionIds.size() - adverseReactions.size();
    if (numberOfAdverseReactionsNotFound > 0) {
      throw new ResourceNotFoundException(numberOfAdverseReactionsNotFound + " adverse reactions were not found");
    }

    return new HashSet<>(adverseReactions);
  }

  public Medicine update(String registrationNumber, MedicineUpdateDto medicineUpdateDto) {
    Medicine medicine = findMedicineByRegistrationNumber(registrationNumber);
    Manufacturer manufacturer = findManufacturerById(medicineUpdateDto.getManufacturerId());
    Set<AdverseReaction> adverseReactions = findAdverseReactionsByIds(medicineUpdateDto.getAdverseReactionIds());

    medicine.setName(medicineUpdateDto.getName());
    medicine.setExpirationDate(medicineUpdateDto.getExpirationDate());
    medicine.setCustomerServicePhone(medicineUpdateDto.getCustomerServicePhone());
    medicine.setPrice(medicineUpdateDto.getPrice());
    medicine.setAmountOfPills(medicineUpdateDto.getAmountOfPills());
    medicine.setManufacturer(manufacturer);
    medicine.setAdverseReactions(adverseReactions);

    return medicineRepository.save(medicine);
  }

  private Medicine findMedicineByRegistrationNumber(String registrationNumber) {
    return medicineRepository.findById(registrationNumber)
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format("Medicine with registration number %s not found", registrationNumber)));
  }

  public void deleteByRegistrationNumber(String registrationNumber) {
    Medicine medicine = findMedicineByRegistrationNumber(registrationNumber);
    medicineRepository.delete(medicine);
  }
}
