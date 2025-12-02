package dk.ek.bcrafteksamensprojekt.dto.Case;

public record CaseMaterialRequestDTO(String description, int quantity, Double unitPrice, Long materialId) {
}
