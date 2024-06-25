package backend.authModule.service;

import backend.authModule.entities.Jeune;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


public interface JeuneService {
    Jeune saveJeune(Jeune jeune);
}
