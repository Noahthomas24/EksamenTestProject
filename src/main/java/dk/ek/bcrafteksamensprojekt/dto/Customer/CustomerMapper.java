package dk.ek.bcrafteksamensprojekt.dto.Customer;

import dk.ek.bcrafteksamensprojekt.model.Customer;
import org.springframework.stereotype.Component;


@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequestDto dto) {
        Customer c = new Customer();
        c.setFirstName(dto.firstName());
        c.setLastName(dto.lastName());
        c.setEmail(dto.email());
        c.setPhoneNumber(dto.phoneNumber());
        c.setAddress(dto.address());
        c.setZipCode(dto.zipCode());
        c.setCity(dto.city());
        return c;
    }

    public CustomerResponseDto toDto(Customer c) {
        return new CustomerResponseDto(
                c.getId(),
                c.getFirstName(),
                c.getLastName(),
                c.getEmail(),
                c.getPhoneNumber(),
                c.getAddress(),
                c.getZipCode(),
                c.getCity()
        );
    }
}
