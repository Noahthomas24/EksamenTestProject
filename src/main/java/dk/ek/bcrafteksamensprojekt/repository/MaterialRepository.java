package dk.ek.bcrafteksamensprojekt.repository;

import dk.ek.bcrafteksamensprojekt.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Long> {
}
