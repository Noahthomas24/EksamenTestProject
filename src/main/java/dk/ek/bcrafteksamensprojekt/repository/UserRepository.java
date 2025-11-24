package dk.ek.bcrafteksamensprojekt.repository;

import dk.ek.bcrafteksamensprojekt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
