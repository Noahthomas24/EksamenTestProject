package dk.ek.bcrafteksamensprojekt.dto.Customer;

public record CustomerResponseDto(Long id, String firstName, String lastName, String email, String phoneNumber, String address, String zipCode, String city) {
}
