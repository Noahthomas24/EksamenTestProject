package dk.ek.bcrafteksamensprojekt.dto.Case;

import java.util.List;

public record CaseRequestDTO(String title, String description, String type, Long customerId, List<CaseMaterialRequestDTO> materials) {
}
