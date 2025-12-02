package dk.ek.bcrafteksamensprojekt.service;

import dk.ek.bcrafteksamensprojekt.dto.Material.MaterialMapper;
import dk.ek.bcrafteksamensprojekt.dto.Material.MaterialRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.Material.MaterialResponseDTO;
import dk.ek.bcrafteksamensprojekt.exceptions.NotFoundException;
import dk.ek.bcrafteksamensprojekt.model.Material;
import dk.ek.bcrafteksamensprojekt.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;

    public MaterialResponseDTO createMaterial(MaterialRequestDTO dto) {
        Material m = materialMapper.toEntity(dto);
        materialRepository.save(m);
        return materialMapper.toDTO(m);
    }

    public MaterialResponseDTO updateMaterial(Long id, MaterialRequestDTO dto) {
        Material m = materialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Material not found: " + id));

        m.setName(dto.name());
        m.setUnit(dto.unit());
        m.setPricePerUnit(dto.pricePerUnit());

        materialRepository.save(m);
        return materialMapper.toDTO(m);
    }

    public MaterialResponseDTO getById(Long id) {
        Material m = materialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Material not found: " + id));

        return materialMapper.toDTO(m);
    }

    public List<MaterialResponseDTO> getAllMaterials() {
        return materialRepository.findAll().stream()
                .map(materialMapper::toDTO)
                .toList();
    }

    public List<MaterialResponseDTO> searchByName(String name) {
        return materialRepository.findByNameContainingIgnoreCase(name).stream()
                .map(materialMapper::toDTO)
                .toList();
    }

    public void delete(Long id) {
        Material m = materialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Material not found: " + id));

        materialRepository.delete(m);
    }
}

