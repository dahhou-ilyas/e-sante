package backend.authModule.repository;

import backend.authModule.entities.ProfessionnelSante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessionnelSanteRepository extends JpaRepository<ProfessionnelSante,Long> {
}
