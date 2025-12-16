package dk.ek.bcrafteksamensprojekt.dto.Case;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CaseFileResponseDTO(Long id, String filename, String originalFilename, String fileType, Long fileSize, LocalDateTime uploadedAt) {
}
