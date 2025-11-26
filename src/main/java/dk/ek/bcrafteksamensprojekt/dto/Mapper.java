package dk.ek.bcrafteksamensprojekt.dto;

import dk.ek.bcrafteksamensprojekt.model.Customer;
import org.springframework.stereotype.Component;


@Component
public class Mapper {




    public Customer customerRequestDtoToCustomer(CustomerRequestDto dto){
        Customer customer = new Customer();

        customer.setFirstName(dto.firstName());
        customer.setLastName(dto.lastName());
        customer.setEmail(dto.email());
        customer.setPhoneNumber(dto.phoneNumber());

        return customer;
    }








}
