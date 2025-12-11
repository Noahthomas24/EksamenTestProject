package dk.ek.bcrafteksamensprojekt.dto.Case;

import dk.ek.bcrafteksamensprojekt.model.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CaseMapper {

    public Case toEntity(CaseRequestDTO dto, Customer customer) {
        Case c = new Case();
        c.setTitle(dto.title());
        c.setDescription(dto.description());
        c.setType(dto.type());
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
                c.getType(),
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

    public CaseFileResponseDTO toCaseFileResponseDTO(CaseFile file) {
        return new CaseFileResponseDTO(
                file.getId(),
                file.getFilename(),
                file.getOriginalFilename(),
                file.getFileType(),
                file.getFileSize(),
                file.getUploadedAt()
        );
    }
}

