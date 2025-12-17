package dk.ek.bcrafteksamensprojekt.controller;

import dk.ek.bcrafteksamensprojekt.dto.Case.CaseResponseDTO;
import dk.ek.bcrafteksamensprojekt.dto.OfferRequest.OfferRequestRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.OfferRequest.OfferRequestResponseDTO;
import dk.ek.bcrafteksamensprojekt.service.OfferRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offer-requests")
@RequiredArgsConstructor
public class OfferRequestController {

    private final OfferRequestService offerRequestService;

    // -------------------------------------------------
    // HENT ALLE OFFER REQUESTS (ADMIN)
    // -------------------------------------------------
    @GetMapping
    public ResponseEntity<List<OfferRequestResponseDTO>> getAll() {
        return ResponseEntity.ok(offerRequestService.getAll());
    }

    // -------------------------------------------------
    // HENT ÉN OFFER REQUEST
    // -------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<OfferRequestResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(offerRequestService.getById(id));
    }

    // -------------------------------------------------
    // OPRET OFFER REQUEST (FRA HJEMMESIDEN)
    // -------------------------------------------------
    @PostMapping
    public ResponseEntity<OfferRequestResponseDTO> create(
            @RequestBody OfferRequestRequestDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(offerRequestService.create(dto));
    }

    // -------------------------------------------------
    // ACCEPT → OPRET SAG + SLET OFFER REQUEST
    // -------------------------------------------------
    @PutMapping("/{id}/accept")
    public ResponseEntity<CaseResponseDTO> accept(@PathVariable Long id) {
        return ResponseEntity.ok(offerRequestService.accept(id));
    }

    // -------------------------------------------------
    // DENY → SLET OFFER REQUEST
    // -------------------------------------------------
    @DeleteMapping("/{id}/deny")
    public ResponseEntity<Void> deny(@PathVariable Long id) {
        offerRequestService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
