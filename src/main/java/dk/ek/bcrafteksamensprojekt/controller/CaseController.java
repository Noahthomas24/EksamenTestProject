package dk.ek.bcrafteksamensprojekt.controller;

import dk.ek.bcrafteksamensprojekt.dto.Case.CaseMaterialRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.Case.CaseRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.Case.CaseResponseDTO;
import dk.ek.bcrafteksamensprojekt.service.CaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cases")
@RequiredArgsConstructor
public class CaseController {

    private final CaseService caseService;

    // Get all cases
    @GetMapping
    public ResponseEntity<List<CaseResponseDTO>> getAllCases() {
        return ResponseEntity.ok(caseService.findAllCases());
    }

    // Get single case
    @GetMapping("/{id}")
    public ResponseEntity<CaseResponseDTO> getCaseById(@PathVariable Long id) {
        return ResponseEntity.ok(caseService.getCaseById(id));
    }

    // Create new case
    @PostMapping
    public ResponseEntity<CaseResponseDTO> createCase(@RequestBody CaseRequestDTO dto) {
        CaseResponseDTO saved = caseService.createCase(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Update existing case
    @PutMapping("/{id}")
    public ResponseEntity<CaseResponseDTO> updateCase(
            @PathVariable Long id,
            @RequestBody CaseRequestDTO dto
    ) {
        return ResponseEntity.ok(caseService.updateCase(id, dto));
    }

    // Delete case
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCase(@PathVariable Long id) {
        caseService.deleteCase(id);
        return ResponseEntity.noContent().build();
    }

    // ----------------- CaseMaterial endpoints -----------------

    @PostMapping("/{id}/materials")
    public ResponseEntity<CaseResponseDTO> addMaterial(
            @PathVariable Long id,
            @RequestBody CaseMaterialRequestDTO dto
    ) {
        return ResponseEntity.ok(caseService.addMaterial(id, dto));
    }

    @PutMapping("/{id}/materials/{caseMaterialId}")
    public ResponseEntity<CaseResponseDTO> updateMaterial(
            @PathVariable Long id,
            @PathVariable Long caseMaterialId,
            @RequestBody CaseMaterialRequestDTO dto
    ) {
        return ResponseEntity.ok(caseService.updateMaterial(id, caseMaterialId, dto));
    }

    @DeleteMapping("/{id}/materials/{caseMaterialId}")
    public ResponseEntity<Void> removeMaterial(
            @PathVariable Long id,
            @PathVariable Long caseMaterialId
    ) {
        caseService.deleteMaterialFromCase(id, caseMaterialId);
        return ResponseEntity.noContent().build();
    }
}

