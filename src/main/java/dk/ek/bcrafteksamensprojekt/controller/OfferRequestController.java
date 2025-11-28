package dk.ek.bcrafteksamensprojekt.controller;

import dk.ek.bcrafteksamensprojekt.model.OfferRequest;
import dk.ek.bcrafteksamensprojekt.service.OfferRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offer-requests")
@RequiredArgsConstructor
public class OfferRequestController {

    private final OfferRequestService offerRequestService;

    // Get all offer requests
    @GetMapping
    public ResponseEntity<List<OfferRequest>> getAllOfferRequests() {
        return ResponseEntity.ok(offerRequestService.findAllOfferRequests());
    }

    // Get single offer request
    @GetMapping("/{id}")
    public ResponseEntity<OfferRequest> getOfferRequestById(@PathVariable Long id) {
        // TODO: e.g. public OfferRequest findOfferRequestById(Long id)
        return ResponseEntity.ok(offerRequestService.findOfferRequestById(id));
    }

    // Create offer request (called from contact form)
    @PostMapping
    public ResponseEntity<OfferRequest> createOfferRequest(@RequestBody OfferRequest offerRequest) {
        OfferRequest saved = offerRequestService.createOfferRequest(offerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Delete offer request
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOfferRequest(@PathVariable Long id) {
        offerRequestService.deleteOfferRequest(id);
        return ResponseEntity.noContent().build();
    }
}
