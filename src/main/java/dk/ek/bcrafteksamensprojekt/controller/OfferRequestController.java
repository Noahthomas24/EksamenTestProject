package dk.ek.bcrafteksamensprojekt.controller;

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

    @GetMapping
    public ResponseEntity<List<OfferRequestResponseDTO>> getAll() {
        return ResponseEntity.ok(offerRequestService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferRequestResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(offerRequestService.getById(id));
    }

    @PostMapping
    public ResponseEntity<OfferRequestResponseDTO> create(
            @RequestBody OfferRequestRequestDTO dto
    ) {
        OfferRequestResponseDTO saved = offerRequestService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        offerRequestService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

