package backend.authModule.repository;

import backend.authModule.entities.AntecedentPersonnel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AntecedentPersonnelRepository extends JpaRepository<AntecedentPersonnel,Long> {
}
