package dk.ek.bcrafteksamensprojekt.controller;

import dk.ek.bcrafteksamensprojekt.model.Case;
import dk.ek.bcrafteksamensprojekt.model.CaseMaterial;
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
    public ResponseEntity<List<Case>> getAllCases() {
        return ResponseEntity.ok(caseService.findAllCases());
    }

    // Get single case
    @GetMapping("/{id}")
    public ResponseEntity<Case> getCaseById(@PathVariable Long id) {
        return ResponseEntity.ok(caseService.findCaseById(id));
    }

    // Create new case ( needs to converted to DTO in requestbody )
    @PostMapping
    public ResponseEntity<Case> createCase(@RequestBody Case c, @RequestBody Long customerId) {
        Case saved = caseService.createCase(customerId, c);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Update existing case
    @PutMapping("/{id}")
    public ResponseEntity<Case> updateCase(@PathVariable Long id, @RequestBody Case c) {
        return ResponseEntity.ok(caseService.updateCase(id, c));
    }

    // Delete case
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCase(@PathVariable Long id) {
        caseService.deleteCase(id);
        return ResponseEntity.noContent().build();
    }

    // -- Methods for caseMaterials --

    // Add material to case
    @PostMapping("/{id}/materials")
    public ResponseEntity<Case> addMaterialToCase(@PathVariable Long id, @RequestBody CaseMaterial caseMaterial) {
        return ResponseEntity.ok(caseService.addMaterialToCase(id, caseMaterial));
    }

    // Remove material from case
    @DeleteMapping("/{id}/materials/{caseMaterialId}")
    public ResponseEntity<Void> removeMaterialFromCase(@PathVariable Long id, @PathVariable Long caseMaterialId) {
        return ResponseEntity.noContent().build();
    }

    // Update material from case
    @PutMapping("/{id}/materials/{caseMaterialId}")
    public ResponseEntity<Case> updateMaterialOnCase(@PathVariable Long id, @PathVariable Long caseMaterialId,
                                                     @RequestBody CaseMaterial caseMaterial){
        return ResponseEntity.ok(caseService.updateMaterialOnCase(id, caseMaterialId, caseMaterial));
    }
}
