package dk.ek.bcrafteksamensprojekt.repository;

import dk.ek.bcrafteksamensprojekt.model.Case;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaseRepository extends JpaRepository<Case, Long> {
}
