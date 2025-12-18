package dk.ek.bcrafteksamensprojekt.controller;

import dk.ek.bcrafteksamensprojekt.dto.Case.CaseFileResponseDTO;
import dk.ek.bcrafteksamensprojekt.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cases/{caseId}/files")
public class CaseFileController {

    private final FileStorageService fileStorage;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CaseFileResponseDTO uploadFile(
            @PathVariable Long caseId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return fileStorage.storeFile(caseId, file);
    }

    @GetMapping
    public List<CaseFileResponseDTO> listFiles(@PathVariable Long caseId) {
        return fileStorage.listFiles(caseId);
    }


    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable Long caseId,
                                             @PathVariable Long fileId) throws IOException {

        return fileStorage.download(caseId,fileId);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(
            @PathVariable Long caseId,
            @PathVariable Long fileId
    ) throws IOException {

        fileStorage.deleteFile(caseId, fileId);
        return ResponseEntity.noContent().build();
    }

}
