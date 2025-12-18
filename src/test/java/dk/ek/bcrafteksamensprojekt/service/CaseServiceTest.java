package dk.ek.bcrafteksamensprojekt.service;

import dk.ek.bcrafteksamensprojekt.dto.Case.CaseMapper;
import dk.ek.bcrafteksamensprojekt.dto.Case.CaseMaterialRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.Case.CaseRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.Case.CaseResponseDTO;
import dk.ek.bcrafteksamensprojekt.exceptions.NotFoundException;
import dk.ek.bcrafteksamensprojekt.model.*;
import dk.ek.bcrafteksamensprojekt.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaseServiceTest {

        @Mock
        private CaseRepository caseRepository;
        @Mock
        private CustomerRepository customerRepository;
        @Mock
        private CaseMaterialRepository caseMaterialRepository;
        @Mock
        private MaterialRepository materialRepository;
        @Mock
        private CaseMapper caseMapper;

        @InjectMocks
        private CaseService caseService;



    @Test
    void createCase_shouldCreateCaseWithMaterials() {
        // Arrange
        Customer customer = new Customer();
        customer.setId(1L);

        Material material = new Material();
        material.setId(10L);
        material.setPricePerUnit(100.0);

        CaseRequestDTO dto = new CaseRequestDTO(
                "Test sag",
                "Beskrivelse",
                Type.WOODCRAFT,
                1L,
                List.of(new CaseMaterialRequestDTO(
                        "Arbejde",
                        2,
                        null, // fallback price
                        10L
                ))
        );

        Case caseEntity = new Case();
        caseEntity.setCaseMaterials(new ArrayList<>());

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(materialRepository.findById(10L)).thenReturn(Optional.of(material));
        when(caseMapper.toEntity(eq(dto), eq(customer))).thenReturn(caseEntity);
        when(caseMapper.toResponseDTO(any(Case.class)))
                .thenReturn(mock(CaseResponseDTO.class));

        // Act
        CaseResponseDTO result = caseService.createCase(dto);

        // Assert
        assertNotNull(result);
        verify(caseRepository).save(caseEntity);
        verify(caseMaterialRepository).save(any(CaseMaterial.class));
    }

    @Test
    void createCase_shouldThrowException_whenCustomerNotFound() {
        // Arrange
        CaseRequestDTO dto = new CaseRequestDTO(
                "Test",
                "Desc",
                Type.WOODCRAFT,
                99L,
                null
        );

        when(customerRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class,
                () -> caseService.createCase(dto));
    }


    @Test
    void updateCaseStatus_shouldUpdateStatus() {
        Case c = new Case();
        c.setId(1L);
        c.setStatus(Status.OPEN);

        when(caseRepository.findById(1L))
                .thenReturn(Optional.of(c));
        when(caseMapper.toResponseDTO(c))
                .thenReturn(mock(CaseResponseDTO.class));

        CaseResponseDTO result =
                caseService.updateCaseStatus(1L, Status.CLOSED);

        assertEquals(Status.CLOSED, c.getStatus());
        verify(caseRepository).save(c);
    }


    @Test
    void getCaseById_shouldReturnCase() {
        Case c = new Case();
        c.setId(1L);

        when(caseRepository.findById(1L)).thenReturn(Optional.of(c));
        when(caseMapper.toResponseDTO(c))
                .thenReturn(mock(CaseResponseDTO.class));

        CaseResponseDTO result = caseService.getCaseById(1L);

        assertNotNull(result);
    }


    @Test
    void deleteCase_shouldThrowException_whenNotFound() {
        when(caseRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> caseService.deleteCase(1L));
    }

}