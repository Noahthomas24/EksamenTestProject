package dk.ek.bcrafteksamensprojekt.dto.OfferRequest;

import dk.ek.bcrafteksamensprojekt.model.Type;

public record OfferRequestRequestDTO(String firstName, String lastName, String phoneNumber, String email, String zipcode, String description, Type type) {
}
