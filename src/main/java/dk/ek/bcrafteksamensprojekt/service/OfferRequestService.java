package dk.ek.bcrafteksamensprojekt.service;

import dk.ek.bcrafteksamensprojekt.exceptions.NotFoundException;
import dk.ek.bcrafteksamensprojekt.model.OfferRequest;
import dk.ek.bcrafteksamensprojekt.repository.OfferRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferRequestService {

    private final OfferRequestRepository offerRequestRepository;

    // Create new offer request (used when form is submitted)
    public OfferRequest createOfferRequest(OfferRequest offerRequest) {
        return offerRequestRepository.save(offerRequest);
    }

    // Update an existing offer request
    public OfferRequest updateOfferRequest(Long id, OfferRequest updated) {
        OfferRequest existing = offerRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tilbudsforespørgsel ikke fundet med id " + id));

        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setPhoneNumber(updated.getPhoneNumber());
        existing.setEmail(updated.getEmail());
        existing.setDescription(updated.getDescription());

        return offerRequestRepository.save(existing);
    }

    // Find single offer request
    public OfferRequest findOfferRequestById(Long id) {
        return offerRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tilbudsforespørgsel ikke fundet med id " + id));
    }

    // Return all offer requests
    public List<OfferRequest> findAllOfferRequests() {
        return offerRequestRepository.findAll();
    }

    // Delete an offer request
    public void deleteOfferRequest(Long id) {
        OfferRequest existing = offerRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Kan ikke finde tilbudsforespørgsel med id " + id));

        offerRequestRepository.delete(existing);
    }

    public OfferRequest saveOfferRequest(OfferRequest offerRequest) {
        return offerRequestRepository.save(offerRequest);
    }
}
