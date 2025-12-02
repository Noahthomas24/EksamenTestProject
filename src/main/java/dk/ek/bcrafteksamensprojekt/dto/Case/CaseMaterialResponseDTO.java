package dk.ek.bcrafteksamensprojekt.dto.Case;

public record CaseMaterialResponseDTO(Long id, String description, int quantity, Double unitPrice, Double effectiveUnitPrice, Long materialId) {
}
