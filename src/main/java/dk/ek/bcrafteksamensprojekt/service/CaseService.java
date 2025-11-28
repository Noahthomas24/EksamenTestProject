package dk.ek.bcrafteksamensprojekt.service;

import dk.ek.bcrafteksamensprojekt.exceptions.NotFoundException;
import dk.ek.bcrafteksamensprojekt.model.Case;
import dk.ek.bcrafteksamensprojekt.model.CaseMaterial;
import dk.ek.bcrafteksamensprojekt.model.Customer;
import dk.ek.bcrafteksamensprojekt.repository.CaseMaterialRepository;
import dk.ek.bcrafteksamensprojekt.repository.CaseRepository;
import dk.ek.bcrafteksamensprojekt.repository.CustomerRepository;
import dk.ek.bcrafteksamensprojekt.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CaseService {
    private final CaseRepository caseRepository;
    private final CustomerRepository customerRepository;
    private final MaterialRepository materialRepository;
    private final CaseMaterialRepository caseMaterialRepository;

    public Case createCase(Long customerId, Case c){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Kunde ikke fundet med id "+customerId));
        c.setCustomer(customer);
        return caseRepository.save(c);
    }


    public Case updateCase(Long id, Case updated) {
        Case existing = caseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sag ikke fundet med med id "+id));

        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());

        return caseRepository.save(existing);
    }

    public Case findCaseById(Long id){
        return caseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sag ikke fundet med med id "+id));
    }

    // @Transactional: Runs this method inside one database transaction so JPA can safely update Case and CaseMaterial together
    // If one of the DB queries fail, they are both rolled back, so one is not updated without the other
    @Transactional
    public Case addMaterialToCase(Long id, CaseMaterial caseMaterial){
        Case c = caseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sag ikke fundet med med id "+id));

        c.addCaseMaterial(caseMaterial);

        return caseRepository.save(c);
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


}
