package backend.authModule.repository;

import backend.authModule.entities.Jeune;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JeuneRepository extends JpaRepository<Jeune,Long> {
}
