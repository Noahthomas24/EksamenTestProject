package dk.ek.bcrafteksamensprojekt.controller;

import dk.ek.bcrafteksamensprojekt.dto.Material.MaterialRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.Material.MaterialResponseDTO;
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

    @GetMapping
    public ResponseEntity<List<MaterialResponseDTO>> getAll() {
        return ResponseEntity.ok(materialService.getAllMaterials());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MaterialResponseDTO>> search(@RequestParam String name) {
        return ResponseEntity.ok(materialService.searchByName(name));
    }

    @PostMapping
    public ResponseEntity<MaterialResponseDTO> create(@RequestBody MaterialRequestDTO dto) {
        System.out.println("DTO RECEIVED = " + dto);
        MaterialResponseDTO saved = materialService.createMaterial(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialResponseDTO> update(
            @PathVariable Long id,
            @RequestBody MaterialRequestDTO dto
    ) {
        return ResponseEntity.ok(materialService.updateMaterial(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

