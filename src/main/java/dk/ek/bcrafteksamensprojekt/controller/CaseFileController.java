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

    @GetMapping("/files/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable Long caseId,
                                             @PathVariable Long fileId) throws IOException {

        return fileStorage.download(caseId,fileId);
    }
}
