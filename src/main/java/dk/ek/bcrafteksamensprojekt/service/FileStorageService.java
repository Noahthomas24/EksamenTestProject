package dk.ek.bcrafteksamensprojekt.service;

import dk.ek.bcrafteksamensprojekt.dto.Case.CaseFileResponseDTO;
import dk.ek.bcrafteksamensprojekt.dto.Case.CaseMapper;
import dk.ek.bcrafteksamensprojekt.model.Case;
import dk.ek.bcrafteksamensprojekt.model.CaseFile;
import dk.ek.bcrafteksamensprojekt.repository.CaseFileRepository;
import dk.ek.bcrafteksamensprojekt.repository.CaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final CaseRepository caseRepo;
    private final CaseFileRepository caseFileRepo;
    private final CaseMapper caseFileMapper;

    public CaseFileResponseDTO storeFile(Long caseId, MultipartFile file) throws IOException {

        Case c = caseRepo.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found"));

        String storedName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path target = Paths.get(uploadDir).resolve(storedName);
        Files.copy(file.getInputStream(), target);

        CaseFile cf = new CaseFile();
        cf.setFilename(storedName);
        cf.setOriginalFilename(file.getOriginalFilename());
        cf.setFileType(file.getContentType());
        cf.setFileSize(file.getSize());
        cf.setC(c);

        CaseFile saved = caseFileRepo.save(cf);

        return caseFileMapper.toCaseFileResponseDTO(saved);
    }


    public ResponseEntity<Resource> download(Long fileId, Long caseId) {
        CaseFile cf = caseFileRepo.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        if (!cf.getC().getId().equals(caseId)) {
            throw new RuntimeException("File does not belong to this case");
        }

        Path filePath = Paths.get(uploadDir).resolve(cf.getFilename());
        Resource resource = new FileSystemResource(filePath);

        if (!resource.exists()) {
            throw new RuntimeException("File missing on server");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(cf.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + cf.getOriginalFilename() + "\"")
                .body(resource);
    }
}
