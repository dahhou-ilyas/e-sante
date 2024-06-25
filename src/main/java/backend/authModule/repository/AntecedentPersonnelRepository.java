package backend.authModule.repository;

import backend.authModule.entities.AntecedentPersonnel;
import backend.authModule.entities.Jeune;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AntecedentPersonnelRepository extends JpaRepository<AntecedentPersonnel,Long> {
    Optional<AntecedentPersonnel> findByJeune(Jeune jeune);
    void deleteByJeune(Jeune jeune);
}
