package dk.ek.bcrafteksamensprojekt.controller;


import dk.ek.bcrafteksamensprojekt.dto.CustomerRequestDto;
import dk.ek.bcrafteksamensprojekt.model.Customer;
import dk.ek.bcrafteksamensprojekt.dto.Mapper;
import dk.ek.bcrafteksamensprojekt.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final Mapper mapper;


    //returns all customers
    @GetMapping
    public ResponseEntity<List<Customer>> findAllCustomers() {
        return ResponseEntity.ok((customerService.findAllCustomers()));
    }


    // Returns customer by id
    @GetMapping("/{id}")
    public ResponseEntity<Customer> findCustomerById(@PathVariable Long id) {
        Customer customer = customerService.findCustomerById(id);
        if (customer == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }


    // creates a new customer
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody CustomerRequestDto customerRequestDto) {
        Customer customer = mapper.customerRequestDtoToCustomer(customerRequestDto);

        Customer savedCustomer = customerService.createCustomer(customer);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable Long id) {
        if (customerService.findCustomerById(id) == null){
            return ResponseEntity.notFound().build();
        }
        customerService.deleteCustomer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
