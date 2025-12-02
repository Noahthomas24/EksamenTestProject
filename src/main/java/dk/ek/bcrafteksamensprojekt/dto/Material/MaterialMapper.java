package dk.ek.bcrafteksamensprojekt.dto.Material;

import dk.ek.bcrafteksamensprojekt.model.Material;
import org.springframework.stereotype.Component;

@Component
public class MaterialMapper {

    public Material toEntity(MaterialRequestDTO dto) {
        Material m = new Material();
        m.setName(dto.name());
        m.setPricePerUnit(dto.pricePerUnit());
        m.setUnit(dto.unit());
        return m;
    }

    public MaterialResponseDTO toDTO(Material m) {
        return new MaterialResponseDTO(
                m.getId(),
                m.getName(),
                m.getPricePerUnit(),
                m.getUnit()
        );
    }
}

