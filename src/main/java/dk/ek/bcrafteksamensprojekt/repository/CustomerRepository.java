package dk.ek.bcrafteksamensprojekt.repository;

import dk.ek.bcrafteksamensprojekt.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
