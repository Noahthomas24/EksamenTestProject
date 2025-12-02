package dk.ek.bcrafteksamensprojekt.dto.Case;

import dk.ek.bcrafteksamensprojekt.model.Case;
import dk.ek.bcrafteksamensprojekt.model.CaseMaterial;
import dk.ek.bcrafteksamensprojekt.model.Customer;
import dk.ek.bcrafteksamensprojekt.model.Type;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CaseMapper {

    public Case toEntity(CaseRequestDTO dto, Customer customer) {
        Case c = new Case();
        c.setTitle(dto.title());
        c.setDescription(dto.description());
        c.setType(Type.valueOf(dto.type()));
        c.setCustomer(customer);
        return c;
    }

    public CaseResponseDTO toResponseDTO(Case c) {
        List<CaseMaterialResponseDTO> materials = c.getCaseMaterials().stream()
                .map(this::toMaterialDTO)
                .toList();

        return new CaseResponseDTO(
                c.getId(),
                c.getTitle(),
                c.getDescription(),
                c.getCreatedAt(),
                c.getType().toString(),
                c.getCustomer().getId(),
                materials
        );
    }

    private CaseMaterialResponseDTO toMaterialDTO(CaseMaterial cm) {
        return new CaseMaterialResponseDTO(
                cm.getId(),
                cm.getDescription(),
                cm.getQuantity(),
                cm.getUnitPrice(),
                cm.getEffectiveUnitPrice(),
                cm.getMaterial().getId()
        );
    }
}

