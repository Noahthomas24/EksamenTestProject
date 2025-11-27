package dk.ek.bcrafteksamensprojekt.service;

import dk.ek.bcrafteksamensprojekt.exceptions.NotFoundException;
import dk.ek.bcrafteksamensprojekt.model.Customer;
import dk.ek.bcrafteksamensprojekt.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    private CustomerRepository customerRepository;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customerService = new CustomerService(customerRepository);
    }

    // ---------- createCustomer ----------

    @Test
    void createCustomer_savesAndReturnsCustomer() {
        // Arrange
        Customer customer = new Customer();
        customer.setFirstName("Hans");
        customer.setLastName("Hansen");
        customer.setEmail("hans@example.com");

        Customer saved = new Customer();
        saved.setId(1L);
        saved.setFirstName("Hans");
        saved.setLastName("Hansen");
        saved.setEmail("hans@example.com");

        when(customerRepository.save(customer)).thenReturn(saved);

        // Act
        Customer result = customerService.createCustomer(customer);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("Hans", result.getFirstName());
        assertEquals("Hansen", result.getLastName());
        assertEquals("hans@example.com", result.getEmail());
        verify(customerRepository).save(customer);
    }

    // ---------- updateCustomer ----------

    @Test
    void updateCustomer_updatesExistingCustomer() {
        // Arrange
        Long id = 1L;

        Customer existing = new Customer();
        existing.setId(id);
        existing.setFirstName("Hans");
        existing.setLastName("Hansen");
        existing.setPhoneNumber("12345678");
        existing.setEmail("old@example.com");
        existing.setAddress("Old street 1");
        existing.setZipCode("8000");
        existing.setCity("Aarhus");

        Customer updated = new Customer();
        updated.setFirstName("Jens");
        updated.setLastName("Jensen");
        updated.setPhoneNumber("87654321");
        updated.setEmail("new@example.com");
        updated.setAddress("New street 2");
        updated.setZipCode("9000");
        updated.setCity("Aalborg");

        when(customerRepository.findById(id)).thenReturn(Optional.of(existing));
        when(customerRepository.save(existing)).thenReturn(existing);

        // Act
        Customer result = customerService.updateCustomer(id, updated);

        // Assert
        assertEquals("Jens", result.getFirstName());
        assertEquals("Jensen", result.getLastName());
        assertEquals("87654321", result.getPhoneNumber());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("New street 2", result.getAddress());
        assertEquals("9000", result.getZipCode());
        assertEquals("Aalborg", result.getCity());

        verify(customerRepository).findById(id);
        verify(customerRepository).save(existing);
    }

    @Test
    void updateCustomer_throwsWhenCustomerNotFound() {
        // Arrange
        Long id = 99L;
        Customer updated = new Customer();

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class,
                () -> customerService.updateCustomer(id, updated));

        verify(customerRepository).findById(id);
        verify(customerRepository, never()).save(any());
    }

    // ---------- findCustomerById ----------

    @Test
    void findCustomerById_returnsCustomerWhenFound() {
        // Arrange
        Long id = 1L;
        Customer customer = new Customer();
        customer.setId(id);
        customer.setFirstName("Hans");
        customer.setLastName("Hansen");

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        // Act
        Customer result = customerService.findCustomerById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Hans", result.getFirstName());
        assertEquals("Hansen", result.getLastName());
        verify(customerRepository).findById(id);
    }

    @Test
    void findCustomerById_throwsWhenNotFound() {
        // Arrange
        Long id = 42L;
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class,
                () -> customerService.findCustomerById(id));

        verify(customerRepository).findById(id);
    }

    // ---------- findCustomersByName ----------

    @Test
    void findCustomersByName_returnsMatchingCustomers_caseInsensitive() {
        // Arrange
        Customer c1 = new Customer();
        c1.setId(1L);
        c1.setFullName("Hans Hansen"); // because service uses getFullName()
        Customer c2 = new Customer();
        c2.setId(2L);
        c2.setFullName("Jens Jensen");
        Customer c3 = new Customer();
        c3.setId(3L);
        c3.setFullName("Lise Hansen");

        when(customerRepository.findAll()).thenReturn(Arrays.asList(c1, c2, c3));

        // Act
        List<Customer> result = customerService.findCustomersByName("hansen");

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.getId().equals(1L)));
        assertTrue(result.stream().anyMatch(c -> c.getId().equals(3L)));
        verify(customerRepository).findAll();
    }

    @Test
    void findCustomersByName_returnsEmptyListWhenNoMatch() {
        // Arrange
        Customer c1 = new Customer();
        c1.setFullName("Hans Hansen");
        Customer c2 = new Customer();
        c2.setFullName("Jens Jensen");

        when(customerRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        // Act
        List<Customer> result = customerService.findCustomersByName("pedersen");

        // Assert
        assertTrue(result.isEmpty());
        verify(customerRepository).findAll();
    }

    // ---------- deleteCustomer ----------

    @Test
    void deleteCustomer_deletesWhenCustomerExists() {
        // Arrange
        Long id = 1L;
        Customer customer = new Customer();
        customer.setId(id);

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        // Act
        customerService.deleteCustomer(id);

        // Assert
        verify(customerRepository).findById(id);
        verify(customerRepository).deleteById(id);
    }

    @Test
    void deleteCustomer_throwsWhenCustomerNotFound() {
        // Arrange
        Long id = 99L;
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class,
                () -> customerService.deleteCustomer(id));

        verify(customerRepository).findById(id);
        verify(customerRepository, never()).deleteById(any());
    }
}
