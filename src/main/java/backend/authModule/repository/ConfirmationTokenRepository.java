package backend.authModule.repository;

import backend.authModule.entities.ConfirmationToken;
import backend.authModule.entities.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long> {
    ConfirmationToken findByToken(String token);

    ConfirmationToken findByMedecin(Medecin medecin);
}
