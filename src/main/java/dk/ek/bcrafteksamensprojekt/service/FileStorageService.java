package dk.ek.bcrafteksamensprojekt.service;

import dk.ek.bcrafteksamensprojekt.dto.Case.CaseFileResponseDTO;
import dk.ek.bcrafteksamensprojekt.dto.Case.CaseMapper;
import dk.ek.bcrafteksamensprojekt.model.Case;
import dk.ek.bcrafteksamensprojekt.model.CaseFile;
import dk.ek.bcrafteksamensprojekt.repository.CaseFileRepository;
import dk.ek.bcrafteksamensprojekt.repository.CaseRepository;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private final CaseRepository caseRepo;
    private final CaseFileRepository caseFileRepo;
    private final CaseMapper caseFileMapper;

    private final Path uploadDir;

    // 🔑 SINGLE constructor — Spring will use this
    public FileStorageService(
            CaseRepository caseRepo,
            CaseFileRepository caseFileRepo,
            CaseMapper caseFileMapper,
            @Value("${FILE_UPLOAD_DIR:uploads}") String uploadDir
    ) throws IOException {
        this.caseRepo = caseRepo;
        this.caseFileRepo = caseFileRepo;
        this.caseFileMapper = caseFileMapper;

        this.uploadDir = Paths.get(uploadDir);
        Files.createDirectories(this.uploadDir); // 🔥 CRITICAL
    }

    // -------------------------------------------------
    // UPLOAD
    // -------------------------------------------------
    public CaseFileResponseDTO storeFile(Long caseId, MultipartFile file) throws IOException {

        Case c = caseRepo.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found"));

        String storedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path target = uploadDir.resolve(storedName);

        Files.copy(file.getInputStream(), target);

        CaseFile cf = new CaseFile();
        cf.setFilename(storedName);
        cf.setOriginalFilename(file.getOriginalFilename());
        cf.setFileType(file.getContentType());
        cf.setFileSize(file.getSize());
        cf.setUploadedAt(LocalDateTime.now());
        cf.setC(c);

        CaseFile saved = caseFileRepo.save(cf);

        return caseFileMapper.toCaseFileResponseDTO(saved);
    }

    // -------------------------------------------------
    // DOWNLOAD
    // -------------------------------------------------
    public ResponseEntity<Resource> download(Long caseId, Long fileId) {

        CaseFile cf = caseFileRepo.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        if (!cf.getC().getId().equals(caseId)) {
            throw new RuntimeException("File does not belong to this case");
        }

        Path filePath = uploadDir.resolve(cf.getFilename());
        Resource resource = new FileSystemResource(filePath);

        if (!resource.exists()) {
            throw new RuntimeException("File missing on server");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(cf.getFileType()))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + cf.getOriginalFilename() + "\""
                )
                .body(resource);
    }

    // -------------------------------------------------
    // LIST FILES FOR CASE
    // -------------------------------------------------
    public List<CaseFileResponseDTO> listFiles(Long caseId) {
        return caseFileRepo.findAllByC_Id(caseId).stream()
                .map(caseFileMapper::toCaseFileResponseDTO)
                .toList();
    }

    // -------------------------------------------------
// DELETE
// -------------------------------------------------
    public void deleteFile(Long caseId, Long fileId) throws IOException {

        CaseFile cf = caseFileRepo.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        // Safety check
        if (!cf.getC().getId().equals(caseId)) {
            throw new RuntimeException("File does not belong to this case");
        }

        // Delete file from disk
        Path filePath = uploadDir.resolve(cf.getFilename());
        Files.deleteIfExists(filePath);

        // Delete DB record
        caseFileRepo.delete(cf);
    }

}