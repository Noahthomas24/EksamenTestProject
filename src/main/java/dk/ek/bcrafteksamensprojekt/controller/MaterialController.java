package dk.ek.bcrafteksamensprojekt.controller;

import dk.ek.bcrafteksamensprojekt.model.Material;
import dk.ek.bcrafteksamensprojekt.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    // Get all materials
    @GetMapping
    public ResponseEntity<List<Material>> getAllMaterials() {
        return ResponseEntity.ok(materialService.findAllMaterials());
    }

    // Get single material
    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.findMaterialById(id));
    }

    // Search materials by name
    @GetMapping("/search")
    public ResponseEntity<List<Material>> searchMaterialsByName(@RequestParam String name) {
        return ResponseEntity.ok(materialService.findMaterialsByName(name));
    }

    // Create material
    @PostMapping
    public ResponseEntity<Material> createMaterial(@RequestBody Material material) {
        Material saved = materialService.saveMaterial(material);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Update material
    @PutMapping("/{id}")
    public ResponseEntity<Material> updateMaterial(@PathVariable Long id, @RequestBody Material material) {
        material.setId(id);
        return ResponseEntity.ok(materialService.saveMaterial(material));
    }

    // Delete material
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterialById(id);
        return ResponseEntity.noContent().build();
    }
}
