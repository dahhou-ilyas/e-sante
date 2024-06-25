package backend.authModule.repository;

import backend.authModule.entities.AntecedentFamilial;
import backend.authModule.entities.Jeune;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AntecedentFamilialRepository extends JpaRepository<AntecedentFamilial,Long> {
    Optional<AntecedentFamilial> findByJeune(Jeune jeune);
    void deleteByJeune(Jeune jeune);
}
