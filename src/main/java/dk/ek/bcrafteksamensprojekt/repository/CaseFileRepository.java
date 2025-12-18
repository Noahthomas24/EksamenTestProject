package dk.ek.bcrafteksamensprojekt.repository;

import dk.ek.bcrafteksamensprojekt.dto.Case.CaseFileResponseDTO;
import dk.ek.bcrafteksamensprojekt.model.CaseFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaseFileRepository extends JpaRepository<CaseFile, Long> {
    List<CaseFile> findAllByC_Id(Long cd);
}
