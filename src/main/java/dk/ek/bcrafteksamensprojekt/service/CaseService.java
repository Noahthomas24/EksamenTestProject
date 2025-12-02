package dk.ek.bcrafteksamensprojekt.service;

import dk.ek.bcrafteksamensprojekt.dto.Case.CaseMapper;
import dk.ek.bcrafteksamensprojekt.dto.Case.CaseMaterialRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.Case.CaseRequestDTO;
import dk.ek.bcrafteksamensprojekt.dto.Case.CaseResponseDTO;
import dk.ek.bcrafteksamensprojekt.exceptions.NotFoundException;
import dk.ek.bcrafteksamensprojekt.model.*;
import dk.ek.bcrafteksamensprojekt.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CaseService {
    private final CaseRepository caseRepository;
    private final CustomerRepository customerRepository;
    private final CaseMaterialRepository caseMaterialRepository;
    private final OfferRequestRepository offerRequestRepository;
    private final MaterialRepository materialRepository;
    private final CaseMapper caseMapper;

    public CaseResponseDTO createCase(CaseRequestDTO dto) {

        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new NotFoundException("Customer not found: " + dto.customerId()));

        Case c = caseMapper.toEntity(dto, customer);

        caseRepository.save(c);

        return caseMapper.toResponseDTO(c);
    }

    public Case createCaseFromOfferRequest(Long offerRequestId) {

        OfferRequest offerRequest = offerRequestRepository.findById(offerRequestId)
                .orElseThrow(() -> new NotFoundException("Offer Request not found"));

        Case c = new Case();
        c.setTitle("Sag for " + offerRequest.getFirstName());
        c.setDescription(offerRequest.getDescription());
        c.setType(offerRequest.getType());

        return caseRepository.save(c);
    }


    public CaseResponseDTO updateCase(Long id, CaseRequestDTO dto) {

        Case c = caseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Case not found: " + id));

        c.setTitle(dto.title());
        c.setDescription(dto.description());
        c.setType(Type.fromString(dto.type()));

        caseRepository.save(c);

        return caseMapper.toResponseDTO(c);
    }

    public List<CaseResponseDTO> findAllCases() {
        return caseRepository.findAll().stream()
                .map(caseMapper::toResponseDTO)
                .toList();
    }

    public CaseResponseDTO getCaseById(Long id) {
        Case c = caseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Case not found: " + id));
        return caseMapper.toResponseDTO(c);
    }

    public void deleteCase(Long id) {
        Case c = caseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sag ikke fundet med med id "+id));

        caseRepository.delete(c);
    }

    // @Transactional: Runs this method inside one database transaction so JPA can safely update Case and CaseMaterial together
    // If one of the DB queries fail, they are both rolled back, so one is not updated without the other
    @Transactional
    public CaseResponseDTO addMaterial(Long caseId, CaseMaterialRequestDTO dto) {

        Case c = caseRepository.findById(caseId)
                .orElseThrow(() -> new NotFoundException("Case not found: " + caseId));

        Material m = materialRepository.findById(dto.materialId())
                .orElseThrow(() -> new NotFoundException("Material not found: " + dto.materialId()));

        CaseMaterial cm = new CaseMaterial();
        cm.setDescription(dto.description());
        cm.setQuantity(dto.quantity());
        cm.setUnitPrice(dto.unitPrice());
        cm.setMaterial(m);
        cm.setC(c);

        caseMaterialRepository.save(cm);

        c.getCaseMaterials().add(cm);
        caseRepository.save(c);

        return caseMapper.toResponseDTO(c);
    }

    @Transactional
    public Case deleteMaterialFromCase(Long id, Long caseMaterialId){
        Case c = caseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sag ikke fundet med med id "+id));

        CaseMaterial caseMaterial = caseMaterialRepository.findById(caseMaterialId)
                .orElseThrow(() -> new NotFoundException("Sagsmateriale ikke fundet med id "+caseMaterialId));

        if (!caseMaterial.getC().getId().equals(id)){
            throw new NotFoundException("Sagsmateriale med id " + caseMaterialId + " tilhører ikke sag "+id);
        }

        c.removeCaseMaterial(caseMaterial);
        caseMaterialRepository.delete(caseMaterial);

        return caseRepository.save(c);
    }

    @Transactional
    public CaseResponseDTO updateMaterial(Long caseId, Long cmId, CaseMaterialRequestDTO dto) {

        CaseMaterial cm = caseMaterialRepository.findById(cmId)
                .orElseThrow(() -> new NotFoundException("CaseMaterial not found: " + cmId));

        cm.setDescription(dto.description());
        cm.setQuantity(dto.quantity());
        cm.setUnitPrice(dto.unitPrice());

        caseMaterialRepository.save(cm);

        Case c = cm.getC();

        return caseMapper.toResponseDTO(c);
    }

}
