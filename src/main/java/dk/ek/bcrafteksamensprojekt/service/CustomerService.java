package dk.ek.bcrafteksamensprojekt.service;

import dk.ek.bcrafteksamensprojekt.dto.Customer.CustomerMapper;
import dk.ek.bcrafteksamensprojekt.dto.Customer.CustomerRequestDto;
import dk.ek.bcrafteksamensprojekt.dto.Customer.CustomerResponseDto;
import dk.ek.bcrafteksamensprojekt.exceptions.NotFoundException;
import dk.ek.bcrafteksamensprojekt.model.Customer;
import dk.ek.bcrafteksamensprojekt.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerResponseDto createCustomer(CustomerRequestDto dto) {
        Customer customer = customerMapper.toEntity(dto);
        customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }



    public CustomerResponseDto updateCustomer(Long id, CustomerRequestDto dto) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found: " + id));

        existing.setFirstName(dto.firstName());
        existing.setLastName(dto.lastName());
        existing.setEmail(dto.email());
        existing.setPhoneNumber(dto.phoneNumber());
        existing.setAddress(dto.address());
        existing.setZipCode(dto.zipCode());
        existing.setCity(dto.city());

        customerRepository.save(existing);

        return customerMapper.toDto(existing);
    }

    // Returns custom NotFoundException
    public CustomerResponseDto findById(Long id) {
        Customer c = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found: " + id));

        return customerMapper.toDto(c);
    }

    // Filters customers to contain search criteria
    public List<Customer> findCustomersByName(String name){
        return customerRepository.findAll().stream()
                .filter(c -> c.getFullName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<CustomerResponseDto> findAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toDto)
                .toList();
    }

    public void deleteCustomer(Long id) {
        Customer c = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found: " + id));

        customerRepository.delete(c);
    }
}
