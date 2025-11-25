package dk.ek.bcrafteksamensprojekt.service;

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

    public Customer createCustomer(Customer customer){
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer updated){
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Kunde ikke fundet med id "+id));

        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setPhoneNumber(updated.getPhoneNumber());
        existing.setEmail(updated.getEmail());
        existing.setAddress(updated.getAddress());
        existing.setZipCode(updated.getZipCode());
        existing.setCity(updated.getCity());

        return customerRepository.save(existing);
    }

    // Returns custom NotFoundException
    public Customer findCustomerById(Long id){
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Kunde ikke fundet med id "+id));
    }

    // Filters customers to contain search criteria
    public List<Customer> findCustomersByName(String name){
        return customerRepository.findAll().stream()
                .filter(c -> c.getFullName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void deleteCustomer(Long id){
        Customer customer = customerRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Kan ikke finde kunde med id "+id));
        customerRepository.deleteById(id);
        }
    }
