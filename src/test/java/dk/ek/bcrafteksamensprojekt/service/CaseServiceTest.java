package dk.ek.bcrafteksamensprojekt.service;

import dk.ek.bcrafteksamensprojekt.exceptions.NotFoundException;
import dk.ek.bcrafteksamensprojekt.model.Case;
import dk.ek.bcrafteksamensprojekt.model.CaseMaterial;
import dk.ek.bcrafteksamensprojekt.model.Customer;
import dk.ek.bcrafteksamensprojekt.repository.CaseMaterialRepository;
import dk.ek.bcrafteksamensprojekt.repository.CaseRepository;
import dk.ek.bcrafteksamensprojekt.repository.CustomerRepository;
import dk.ek.bcrafteksamensprojekt.repository.MaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CaseServiceTest {

    private CaseRepository caseRepository;
    private CustomerRepository customerRepository;
    private MaterialRepository materialRepository;
    private CaseMaterialRepository caseMaterialRepository;

    private CaseService caseService;

    @BeforeEach
    void setUp() {
        caseRepository = mock(CaseRepository.class);
        customerRepository = mock(CustomerRepository.class);
        materialRepository = mock(MaterialRepository.class);
        caseMaterialRepository = mock(CaseMaterialRepository.class);

        caseService = new CaseService(
                caseRepository,
                customerRepository,
                materialRepository,
                caseMaterialRepository
        );
    }

    // ---------- createCase ----------

    @Test
    void createCase_createsCaseForExistingCustomer() {
        // Arrange
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);

        Case c = new Case();
        c.setTitle("New kitchen");
        c.setDescription("Custom oak kitchen");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(caseRepository.save(c)).thenReturn(c);

        // Act
        Case result = caseService.createCase(customerId, c);

        // Assert
        assertEquals(customer, result.getCustomer());
        verify(customerRepository).findById(customerId);
        verify(caseRepository).save(c);
    }

    @Test
    void createCase_throwsWhenCustomerNotFound() {
        // Arrange
        Long customerId = 99L;
        Case c = new Case();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class,
                () -> caseService.createCase(customerId, c));

        verify(customerRepository).findById(customerId);
        verify(caseRepository, never()).save(any());
    }

    // ---------- updateCase ----------

    @Test
    void updateCase_updatesTitleAndDescription() {
        // Arrange
        Long caseId = 1L;

        Case existing = new Case();
        existing.setId(caseId);
        existing.setTitle("Old title");
        existing.setDescription("Old description");

        Case updated = new Case();
        updated.setTitle("New title");
        updated.setDescription("New description");

        when(caseRepository.findById(caseId)).thenReturn(Optional.of(existing));
        when(caseRepository.save(existing)).thenReturn(existing);

        // Act
        Case result = caseService.updateCase(caseId, updated);

        // Assert
        assertEquals("New title", result.getTitle());
        assertEquals("New description", result.getDescription());
        verify(caseRepository).findById(caseId);
        verify(caseRepository).save(existing);
    }

    @Test
    void updateCase_throwsWhenCaseNotFound() {
        // Arrange
        Long caseId = 42L;
        Case updated = new Case();

        when(caseRepository.findById(caseId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class,
                () -> caseService.updateCase(caseId, updated));

        verify(caseRepository).findById(caseId);
        verify(caseRepository, never()).save(any());
    }

    // ---------- findCaseById ----------

    @Test
    void findCaseById_returnsCaseWhenFound() {
        // Arrange
        Long caseId = 1L;
        Case c = new Case();
        c.setId(caseId);
        c.setTitle("Some case");
        c.setCreatedAt(LocalDate.now());

        when(caseRepository.findById(caseId)).thenReturn(Optional.of(c));

        // Act
        Case result = caseService.findCaseById(caseId);

        // Assert
        assertNotNull(result);
        assertEquals(caseId, result.getId());
        verify(caseRepository).findById(caseId);
    }

    @Test
    void findCaseById_throwsWhenNotFound() {
        // Arrange
        Long caseId = 99L;
        when(caseRepository.findById(caseId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class,
                () -> caseService.findCaseById(caseId));

        verify(caseRepository).findById(caseId);
    }

    // ---------- addMaterialToCase ----------

    @Test
    void addMaterialToCase_addsCaseMaterialAndSavesCase() {
        // Arrange
        Long caseId = 1L;
        Case c = new Case();
        c.setId(caseId);

        CaseMaterial cm = new CaseMaterial();
        cm.setDescription("Oak plank 5m");
        cm.setQuantity(10);

        when(caseRepository.findById(caseId)).thenReturn(Optional.of(c));
        when(caseRepository.save(c)).thenReturn(c);

        // Act
        Case result = caseService.addMaterialToCase(caseId, cm);

        // Assert
        // We can't see inside addCaseMaterial() easily, but we CAN verify save is called
        verify(caseRepository).findById(caseId);
        verify(caseRepository).save(c);
    }

    @Test
    void addMaterialToCase_throwsWhenCaseNotFound() {
        // Arrange
        Long caseId = 99L;
        CaseMaterial cm = new CaseMaterial();

        when(caseRepository.findById(caseId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class,
                () -> caseService.addMaterialToCase(caseId, cm));

        verify(caseRepository).findById(caseId);
        verify(caseRepository, never()).save(any());
    }

    // ---------- deleteMaterialFromCase ----------

    @Test
    void deleteMaterialFromCase_deletesMaterialAndSavesCase() {
        // Arrange
        Long caseId = 1L;
        Long caseMaterialId = 10L;

        Case c = new Case();
        c.setId(caseId);

        CaseMaterial cm = new CaseMaterial();
        cm.setId(caseMaterialId);
        cm.setC(c); // important: belongs to this case

        when(caseRepository.findById(caseId)).thenReturn(Optional.of(c));
        when(caseMaterialRepository.findById(caseMaterialId)).thenReturn(Optional.of(cm));
        when(caseRepository.save(c)).thenReturn(c);

        // Act
        Case result = caseService.deleteMaterialFromCase(caseId, caseMaterialId);

        // Assert
        verify(caseRepository).findById(caseId);
        verify(caseMaterialRepository).findById(caseMaterialId);
        verify(caseMaterialRepository).delete(cm);
        verify(caseRepository).save(c);
    }

    @Test
    void deleteMaterialFromCase_throwsWhenCaseNotFound() {
        // Arrange
        Long caseId = 1L;
        Long caseMaterialId = 10L;

        when(caseRepository.findById(caseId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class,
                () -> caseService.deleteMaterialFromCase(caseId, caseMaterialId));

        verify(caseRepository).findById(caseId);
        verify(caseMaterialRepository, never()).findById(any());
        verify(caseMaterialRepository, never()).delete(any());
    }

    @Test
    void deleteMaterialFromCase_throwsWhenCaseMaterialNotFound() {
        // Arrange
        Long caseId = 1L;
        Long caseMaterialId = 10L;

        Case c = new Case();
        c.setId(caseId);

        when(caseRepository.findById(caseId)).thenReturn(Optional.of(c));
        when(caseMaterialRepository.findById(caseMaterialId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class,
                () -> caseService.deleteMaterialFromCase(caseId, caseMaterialId));

        verify(caseRepository).findById(caseId);
        verify(caseMaterialRepository).findById(caseMaterialId);
        verify(caseMaterialRepository, never()).delete(any());
        verify(caseRepository, never()).save(any());
    }

    @Test
    void deleteMaterialFromCase_throwsWhenCaseMaterialBelongsToOtherCase() {
        // Arrange
        Long caseId = 1L;
        Long caseMaterialId = 10L;

        Case c = new Case();
        c.setId(caseId);

        Case otherCase = new Case();
        otherCase.setId(2L);

        CaseMaterial cm = new CaseMaterial();
        cm.setId(caseMaterialId);
        cm.setC(otherCase); // belongs to another case

        when(caseRepository.findById(caseId)).thenReturn(Optional.of(c));
        when(caseMaterialRepository.findById(caseMaterialId)).thenReturn(Optional.of(cm));

        // Act + Assert
        assertThrows(NotFoundException.class,
                () -> caseService.deleteMaterialFromCase(caseId, caseMaterialId));

        verify(caseRepository).findById(caseId);
        verify(caseMaterialRepository).findById(caseMaterialId);
        verify(caseMaterialRepository, never()).delete(any());
        verify(caseRepository, never()).save(any());
    }
}
