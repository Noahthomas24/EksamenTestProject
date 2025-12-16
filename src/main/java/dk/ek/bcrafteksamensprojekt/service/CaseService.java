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

    // -------------------------------------------------
    // OPRET SAG MED MATERIALER
    // -------------------------------------------------
    @Transactional
    public CaseResponseDTO createCase(CaseRequestDTO dto) {

        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() ->
                        new NotFoundException("Customer not found: " + dto.customerId())
                );

        Case c = caseMapper.toEntity(dto, customer);
        c.setStatus(Status.OPEN);
        caseRepository.save(c);

        //  OPRET CASE-MATERIALER
        if (dto.materials() != null) {
            for (CaseMaterialRequestDTO mDto : dto.materials()) {

                Material material = materialRepository.findById(mDto.materialId())
                        .orElseThrow(() ->
                                new NotFoundException("Material not found: " + mDto.materialId())
                        );

                CaseMaterial cm = new CaseMaterial();
                cm.setDescription(mDto.description());
                cm.setQuantity(mDto.quantity());

                // ⭐ VIGTIGT: fallback til materialets pris
                Double unitPrice = mDto.unitPrice() != null
                        ? mDto.unitPrice()
                        : material.getPricePerUnit();

                cm.setUnitPrice(unitPrice);
                cm.setMaterial(material);
                cm.setC(c);

                caseMaterialRepository.save(cm);
                c.getCaseMaterials().add(cm);
            }
        }

        return caseMapper.toResponseDTO(c);
    }

    // -------------------------------------------------
    // OPRET SAG FRA OFFER REQUEST
    // -------------------------------------------------
    public Case createCaseFromOfferRequest(Long offerRequestId) {

        OfferRequest offerRequest = offerRequestRepository.findById(offerRequestId)
                .orElseThrow(() -> new NotFoundException("Offer Request not found"));

        Case c = new Case();
        c.setTitle("Sag for " + offerRequest.getFirstName());
        c.setDescription(offerRequest.getDescription());
        c.setType(offerRequest.getType());
        c.setStatus(Status.OPEN);

        return caseRepository.save(c);
    }

    // -------------------------------------------------
    // OPDATÉR SAG (INFO – IKKE MATERIALER)
    // -------------------------------------------------
    @Transactional
    public CaseResponseDTO updateCase(Long id, CaseRequestDTO dto) {

        Case c = caseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Case not found: " + id));

        c.setTitle(dto.title());
        c.setDescription(dto.description());
        c.setType(dto.type());

        // --- HÅNDTER MATERIALER ---
        c.getCaseMaterials().clear();

        if (dto.materials() != null) {
            for (CaseMaterialRequestDTO mDto : dto.materials()) {

                Material material = materialRepository.findById(mDto.materialId())
                        .orElseThrow(() ->
                                new NotFoundException("Material not found: " + mDto.materialId())
                        );

                CaseMaterial cm = new CaseMaterial();
                cm.setDescription(mDto.description());
                cm.setQuantity(mDto.quantity());
                cm.setUnitPrice(
                        mDto.unitPrice() != null && mDto.unitPrice() > 0
                                ? mDto.unitPrice()
                                : material.getPricePerUnit()
                );
                cm.setMaterial(material);
                cm.setC(c);

                c.getCaseMaterials().add(cm);
            }
        }

        return caseMapper.toResponseDTO(c);
    }


    // -------------------------------------------------
    // HENT ALLE SAGER
    // -------------------------------------------------
    public List<CaseResponseDTO> findAllCases() {
        return caseRepository.findAll().stream()
                .map(caseMapper::toResponseDTO)
                .toList();
    }

    // -------------------------------------------------
    // HENT ÉN SAG
    // -------------------------------------------------
    public CaseResponseDTO getCaseById(Long id) {
        Case c = caseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Case not found: " + id));
        return caseMapper.toResponseDTO(c);
    }

    // -------------------------------------------------
    // SLET SAG
    // -------------------------------------------------
    public void deleteCase(Long id) {
        Case c = caseRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Sag ikke fundet med id " + id)
                );
        caseRepository.delete(c);
    }

    // -------------------------------------------------
    // TILFØJ MATERIALE TIL SAG
    // -------------------------------------------------
    @Transactional
    public CaseResponseDTO addMaterial(Long caseId, CaseMaterialRequestDTO dto) {

        Case c = caseRepository.findById(caseId)
                .orElseThrow(() ->
                        new NotFoundException("Case not found: " + caseId)
                );

        Material material = materialRepository.findById(dto.materialId())
                .orElseThrow(() ->
                        new NotFoundException("Material not found: " + dto.materialId())
                );

        CaseMaterial cm = new CaseMaterial();
        cm.setDescription(dto.description());
        cm.setQuantity(dto.quantity());

        Double unitPrice = dto.unitPrice() != null
                ? dto.unitPrice()
                : material.getPricePerUnit();

        cm.setUnitPrice(
                dto.unitPrice() != null && dto.unitPrice() > 0
                        ? dto.unitPrice()
                        : null
        );
        cm.setMaterial(material);
        cm.setC(c);

        caseMaterialRepository.save(cm);
        c.getCaseMaterials().add(cm);

        return caseMapper.toResponseDTO(c);
    }

    // -------------------------------------------------
    // FJERN MATERIALE FRA SAG
    // -------------------------------------------------
    @Transactional
    public Case deleteMaterialFromCase(Long id, Long caseMaterialId) {

        Case c = caseRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Sag ikke fundet med id " + id)
                );

        CaseMaterial caseMaterial = caseMaterialRepository.findById(caseMaterialId)
                .orElseThrow(() ->
                        new NotFoundException("Sagsmateriale ikke fundet med id " + caseMaterialId)
                );

        if (!caseMaterial.getC().getId().equals(id)) {
            throw new NotFoundException(
                    "Sagsmateriale med id " + caseMaterialId + " tilhører ikke sag " + id
            );
        }

        c.removeCaseMaterial(caseMaterial);
        caseMaterialRepository.delete(caseMaterial);

        return caseRepository.save(c);
    }

    // -------------------------------------------------
    // OPDATÉR SAGSMATERIALE
    // -------------------------------------------------
    @Transactional
    public CaseResponseDTO updateMaterial(Long caseId, Long cmId, CaseMaterialRequestDTO dto) {

        CaseMaterial cm = caseMaterialRepository.findById(cmId)
                .orElseThrow(() ->
                        new NotFoundException("CaseMaterial not found: " + cmId)
                );

        Material material = cm.getMaterial();

        cm.setDescription(dto.description());
        cm.setQuantity(dto.quantity());

        Double unitPrice = dto.unitPrice() != null
                ? dto.unitPrice()
                : material.getPricePerUnit();

        cm.setUnitPrice(unitPrice);

        caseMaterialRepository.save(cm);

        return caseMapper.toResponseDTO(cm.getC());
    }

    // -------------------------------------------------
    // OPDATÉR SAG STATUS
    // -------------------------------------------------
    public CaseResponseDTO updateCaseStatus(Long id, Status status) {

        Case c = caseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Case not found"));

        c.setStatus(status);
        caseRepository.save(c);

        return caseMapper.toResponseDTO(c);
    }
}
