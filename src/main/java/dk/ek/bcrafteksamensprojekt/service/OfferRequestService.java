package dk.ek.bcrafteksamensprojekt.service;

import dk.ek.bcrafteksamensprojekt.dto.OfferRequest.OfferRequestMapper;
import dk.ek.bcrafteksamensprojekt.dto.OfferRequest.OfferRequestRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.OfferRequest.OfferRequestResponseDTO;
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
    private final OfferRequestMapper offerRequestMapper;

    public OfferRequestResponseDTO create(OfferRequestRequestDTO dto) {
        OfferRequest o = offerRequestMapper.toEntity(dto);
        offerRequestRepository.save(o);
        return offerRequestMapper.toDTO(o);
    }

    public OfferRequestResponseDTO getById(Long id) {
        OfferRequest o = offerRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Offer request not found: " + id));
        return offerRequestMapper.toDTO(o);
    }

    public List<OfferRequestResponseDTO> getAll() {
        return offerRequestRepository.findAll().stream()
                .map(offerRequestMapper::toDTO)
                .toList();
    }

    public void delete(Long id) {
        OfferRequest o = offerRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Offer request not found: " + id));
        offerRequestRepository.delete(o);
    }
}

