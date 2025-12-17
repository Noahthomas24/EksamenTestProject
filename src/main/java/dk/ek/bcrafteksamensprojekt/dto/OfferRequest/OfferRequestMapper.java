package dk.ek.bcrafteksamensprojekt.dto.OfferRequest;

import dk.ek.bcrafteksamensprojekt.model.OfferRequest;
import dk.ek.bcrafteksamensprojekt.model.Type;
import org.springframework.stereotype.Component;

@Component
public class OfferRequestMapper {

    public OfferRequest toEntity(OfferRequestRequestDTO dto) {
        OfferRequest o = new OfferRequest();
        o.setFirstName(dto.firstName());
        o.setLastName(dto.lastName());
        o.setEmail(dto.email());
        o.setZipcode(dto.zipcode());
        o.setPhoneNumber(dto.phoneNumber());
        o.setDescription(dto.description());
        o.setType(dto.type());

        return o;
    }

    public OfferRequestResponseDTO toDTO(OfferRequest e) {
        return new OfferRequestResponseDTO(
                e.getId(),
                e.getFirstName(),
                e.getLastName(),
                e.getPhoneNumber(),
                e.getEmail(),
                e.getZipcode(),
                e.getDescription(),
                e.getType()
        );
    }
}

