package dk.ek.bcrafteksamensprojekt.service;

import dk.ek.bcrafteksamensprojekt.exceptions.NotFoundException;
import dk.ek.bcrafteksamensprojekt.model.Material;
import dk.ek.bcrafteksamensprojekt.repository.MaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MaterialServiceTest {

    private MaterialRepository materialRepository;
    private MaterialService materialService;

    @BeforeEach
    void setUp() {
        materialRepository = mock(MaterialRepository.class);
        materialService = new MaterialService(materialRepository);
    }

    @Test
    void saveMaterial() {
    }

    @Test
    void updateMaterial() {
        // Arrange
        Long id = 1L;

        Material existing = new Material(id, "Oak Wood", 50.0, "meter");
        Material updated = new Material(null, "Pine Wood", 40.0, "piece");

        when(materialRepository.findById(id)).thenReturn(Optional.of(existing));
        when(materialRepository.save(existing)).thenReturn(existing);

        // Act
        Material result = materialService.updateMaterial(id, updated);

        // Assert
        assertEquals("Pine Wood", result.getName());
        assertEquals(40.0, result.getPricePerUnit());
        assertEquals("piece", result.getUnit());

        verify(materialRepository).save(existing);

    }

    @Test
    void findMaterialById() {
    }

    @Test
    void findMaterialsByName() {
    }

    @Test
    void deleteMaterialById() {
    }
}