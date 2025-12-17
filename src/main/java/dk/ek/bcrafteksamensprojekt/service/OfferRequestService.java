package dk.ek.bcrafteksamensprojekt.service;

import dk.ek.bcrafteksamensprojekt.dto.Case.CaseMapper;
import dk.ek.bcrafteksamensprojekt.dto.Case.CaseResponseDTO;
import dk.ek.bcrafteksamensprojekt.dto.OfferRequest.OfferRequestMapper;
import dk.ek.bcrafteksamensprojekt.dto.OfferRequest.OfferRequestRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.OfferRequest.OfferRequestResponseDTO;
import dk.ek.bcrafteksamensprojekt.exceptions.NotFoundException;
import dk.ek.bcrafteksamensprojekt.model.Case;
import dk.ek.bcrafteksamensprojekt.model.Customer;
import dk.ek.bcrafteksamensprojekt.model.OfferRequest;
import dk.ek.bcrafteksamensprojekt.model.Status;
import dk.ek.bcrafteksamensprojekt.repository.CaseRepository;
import dk.ek.bcrafteksamensprojekt.repository.CustomerRepository;
import dk.ek.bcrafteksamensprojekt.repository.OfferRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferRequestService {

    private final OfferRequestRepository offerRequestRepository;
    private final CaseRepository caseRepository;
    private final OfferRequestMapper offerRequestMapper;
    private final CaseMapper caseMapper;
    private final CustomerRepository customerRepository;

    // -------------------------------------------------
    // OPRET OFFER REQUEST
    // -------------------------------------------------
    public OfferRequestResponseDTO create(OfferRequestRequestDTO dto) {
        OfferRequest o = offerRequestMapper.toEntity(dto);
        offerRequestRepository.save(o);
        return offerRequestMapper.toDTO(o);
    }

    // -------------------------------------------------
    // HENT ÉN
    // -------------------------------------------------
    public OfferRequestResponseDTO getById(Long id) {
        OfferRequest o = offerRequestRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Offer request not found: " + id)
                );
        return offerRequestMapper.toDTO(o);
    }

    // -------------------------------------------------
    // HENT ALLE
    // -------------------------------------------------
    public List<OfferRequestResponseDTO> getAll() {
        return offerRequestRepository.findAll().stream()
                .map(offerRequestMapper::toDTO)
                .toList();
    }

    // -------------------------------------------------
    // ACCEPT → OPRET SAG + SLET OFFER REQUEST
    // -------------------------------------------------
    @Transactional
    public CaseResponseDTO accept(Long offerRequestId) {

        OfferRequest o = offerRequestRepository.findById(offerRequestId)
                .orElseThrow(() ->
                        new NotFoundException("Offer request not found: " + offerRequestId)
                );

        Customer customer = new Customer();
        customer.setFirstName(o.getFirstName());
        customer.setLastName(o.getLastName());
        customer.setEmail(o.getEmail());
        customer.setZipCode(o.getZipcode());
        customer.setPhoneNumber(o.getPhoneNumber());

        customerRepository.save(customer);

        Case c = new Case();
        c.setTitle("Sag for " + o.getFirstName() + " " + o.getLastName());
        c.setDescription(o.getDescription());
        c.setType(o.getType());
        c.setStatus(Status.OPEN);
        c.setCustomer(customer);

        caseRepository.save(c);

        offerRequestRepository.delete(o);

        return caseMapper.toResponseDTO(c);
    }

    // -------------------------------------------------
    // SLET (DENY)
    // -------------------------------------------------
    public void delete(Long id) {
        OfferRequest o = offerRequestRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Offer request not found: " + id)
                );
        offerRequestRepository.delete(o);
    }
}
