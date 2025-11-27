package dk.ek.bcrafteksamensprojekt.service;

import dk.ek.bcrafteksamensprojekt.exceptions.NotFoundException;
import dk.ek.bcrafteksamensprojekt.model.Material;
import dk.ek.bcrafteksamensprojekt.repository.MaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
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
        // Arrange
        Material material = new Material(null, "Oak plank", 50.0, "meter");
        Material savedMaterial = new Material(1L, "Oak plank", 50.0, "meter");

        when(materialRepository.save(material)).thenReturn(savedMaterial);

        // Act
        Material result = materialService.saveMaterial(material);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("Oak plank", result.getName());
        assertEquals(50.0, result.getPricePerUnit());
        assertEquals("meter", result.getUnit());

        verify(materialRepository).save(material);
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
    void findMaterialById_returnsMaterial_whenFound() {
        // Arrange
        Long id = 1L;
        Material material = new Material(id, "Oak plank", 50.0, "meter");

        when(materialRepository.findById(id)).thenReturn(Optional.of(material));

        // Act
        Material result = materialService.findMaterialById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Oak plank", result.getName());
        verify(materialRepository).findById(id);
    }

    @Test
    void findMaterialById_throwsNotFound_whenNotFound() {
        // Arrange
        Long id = 42L;
        when(materialRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class, () -> materialService.findMaterialById(id));
        verify(materialRepository).findById(id);
    }

    @Test
    void findMaterialsByName_returnsMatchingMaterials() {
        // Arrange
        Material m1 = new Material(1L, "Oak wood plank", 50.0, "meter");
        Material m2 = new Material(2L, "Pine board", 40.0, "meter");
        Material m3 = new Material(3L, "Birch plywood", 60.0, "sheet");

        when(materialRepository.findAll()).thenReturn(Arrays.asList(m1, m2, m3));

        // Act
        List<Material> result = materialService.findMaterialsByName("wood");

        // Assert
        // current implementation: m.getName().contains(name.toLowerCase())
        // so only "Oak wood plank" contains "wood" exactly
        assertEquals(2, result.size());
        assertEquals("Oak wood plank", result.get(0).getName());
        verify(materialRepository).findAll();
    }


    @Test
    void findMaterialsByName_returnsEmptyList_whenNoMatch() {
        // Arrange
        Material m1 = new Material(1L, "Oak plank", 50.0, "meter");
        Material m2 = new Material(2L, "Pine board", 40.0, "meter");

        when(materialRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        // Act
        List<Material> result = materialService.findMaterialsByName("screw");

        // Assert
        assertTrue(result.isEmpty());
        verify(materialRepository).findAll();
    }

    @Test
    void deleteMaterialById_deletes_whenFound() {
        // Arrange
        Long id = 1L;
        Material material = new Material(id, "Oak plank", 50.0, "meter");

        when(materialRepository.findById(id)).thenReturn(Optional.of(material));

        // Act
        materialService.deleteMaterialById(id);

        // Assert
        verify(materialRepository).findById(id);
        verify(materialRepository).delete(material);
    }

    @Test
    void deleteMaterialById_throwsNotFound_whenNotFound() {
        // Arrange
        Long id = 99L;
        when(materialRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class, () -> materialService.deleteMaterialById(id));

        verify(materialRepository).findById(id);
        verify(materialRepository, never()).delete(any());
    }
}
