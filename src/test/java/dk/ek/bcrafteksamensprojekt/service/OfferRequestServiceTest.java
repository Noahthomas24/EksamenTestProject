package dk.ek.bcrafteksamensprojekt.service;

import dk.ek.bcrafteksamensprojekt.exceptions.NotFoundException;
import dk.ek.bcrafteksamensprojekt.model.OfferRequest;
import dk.ek.bcrafteksamensprojekt.repository.OfferRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OfferRequestServiceTest {

    private OfferRequestRepository offerRequestRepository;
    private OfferRequestService offerRequestService;

    @BeforeEach
    void setUp() {
        offerRequestRepository = mock(OfferRequestRepository.class);
        offerRequestService = new OfferRequestService(offerRequestRepository);
    }

    // ---------- createOfferRequest ----------

    @Test
    void createOfferRequest_savesAndReturnsEntity() {
        // Arrange
        OfferRequest request = new OfferRequest();
        request.setFirstName("Hans");
        request.setLastName("Hansen");
        request.setPhoneNumber("12345678");
        request.setEmail("hans@example.com");
        request.setDescription("Ny terrasse i lærketræ");

        OfferRequest saved = new OfferRequest();
        saved.setId(1L);
        saved.setFirstName("Hans");
        saved.setLastName("Hansen");
        saved.setPhoneNumber("12345678");
        saved.setEmail("hans@example.com");
        saved.setDescription("Ny terrasse i lærketræ");

        when(offerRequestRepository.save(request)).thenReturn(saved);

        // Act
        OfferRequest result = offerRequestService.createOfferRequest(request);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("Hans", result.getFirstName());
        assertEquals("Hansen", result.getLastName());
        assertEquals("12345678", result.getPhoneNumber());
        assertEquals("hans@example.com", result.getEmail());
        assertEquals("Ny terrasse i lærketræ", result.getDescription());
        verify(offerRequestRepository).save(request);
    }

    // ---------- updateOfferRequest ----------

    @Test
    void updateOfferRequest_updatesExistingOfferRequest() {
        // Arrange
        Long id = 1L;

        OfferRequest existing = new OfferRequest();
        existing.setId(id);
        existing.setFirstName("Hans");
        existing.setLastName("Hansen");
        existing.setPhoneNumber("12345678");
        existing.setEmail("old@example.com");
        existing.setDescription("Gammelt projekt");

        OfferRequest updated = new OfferRequest();
        updated.setFirstName("Jens");
        updated.setLastName("Jensen");
        updated.setPhoneNumber("87654321");
        updated.setEmail("new@example.com");
        updated.setDescription("Nyt projekt");

        when(offerRequestRepository.findById(id)).thenReturn(Optional.of(existing));
        when(offerRequestRepository.save(existing)).thenReturn(existing);

        // Act
        OfferRequest result = offerRequestService.updateOfferRequest(id, updated);

        // Assert
        assertEquals("Jens", result.getFirstName());
        assertEquals("Jensen", result.getLastName());
        assertEquals("87654321", result.getPhoneNumber());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("Nyt projekt", result.getDescription());

        verify(offerRequestRepository).findById(id);
        verify(offerRequestRepository).save(existing);
    }

    @Test
    void updateOfferRequest_throwsWhenNotFound() {
        // Arrange
        Long id = 99L;
        OfferRequest updated = new OfferRequest();

        when(offerRequestRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class,
                () -> offerRequestService.updateOfferRequest(id, updated));

        verify(offerRequestRepository).findById(id);
        verify(offerRequestRepository, never()).save(any());
    }

    // ---------- findOfferRequestById ----------

    @Test
    void findOfferRequestById_returnsRequestWhenFound() {
        // Arrange
        Long id = 1L;
        OfferRequest existing = new OfferRequest();
        existing.setId(id);
        existing.setFirstName("Hans");

        when(offerRequestRepository.findById(id)).thenReturn(Optional.of(existing));

        // Act
        OfferRequest result = offerRequestService.findOfferRequestById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Hans", result.getFirstName());
        verify(offerRequestRepository).findById(id);
    }

    @Test
    void findOfferRequestById_throwsWhenNotFound() {
        // Arrange
        Long id = 42L;
        when(offerRequestRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class,
                () -> offerRequestService.findOfferRequestById(id));

        verify(offerRequestRepository).findById(id);
    }

    // ---------- findAllOfferRequests ----------

    @Test
    void findAllOfferRequests_returnsAll() {
        // Arrange
        OfferRequest r1 = new OfferRequest();
        r1.setId(1L);
        OfferRequest r2 = new OfferRequest();
        r2.setId(2L);

        when(offerRequestRepository.findAll()).thenReturn(Arrays.asList(r1, r2));

        // Act
        List<OfferRequest> result = offerRequestService.findAllOfferRequests();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(r -> r.getId().equals(1L)));
        assertTrue(result.stream().anyMatch(r -> r.getId().equals(2L)));
        verify(offerRequestRepository).findAll();
    }

    // ---------- deleteOfferRequest ----------

    @Test
    void deleteOfferRequest_deletesWhenFound() {
        // Arrange
        Long id = 1L;
        OfferRequest existing = new OfferRequest();
        existing.setId(id);

        when(offerRequestRepository.findById(id)).thenReturn(Optional.of(existing));

        // Act
        offerRequestService.deleteOfferRequest(id);

        // Assert
        verify(offerRequestRepository).findById(id);
        verify(offerRequestRepository).delete(existing);
    }

    @Test
    void deleteOfferRequest_throwsWhenNotFound() {
        // Arrange
        Long id = 99L;
        when(offerRequestRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class,
                () -> offerRequestService.deleteOfferRequest(id));

        verify(offerRequestRepository).findById(id);
        verify(offerRequestRepository, never()).delete(any());
    }
}
