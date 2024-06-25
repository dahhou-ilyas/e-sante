package backend.authModule.service;

import backend.authModule.entities.AntecedentFamilial;
import backend.authModule.entities.Jeune;
import backend.authModule.exception.EmailNonValideException;
import backend.authModule.exception.PhoneNonValideException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


public interface JeuneService {
    Jeune saveJeune(Jeune jeune) throws EmailNonValideException, PhoneNonValideException;

    AntecedentFamilial addAntecedentFamilial(Long jeuneId, AntecedentFamilial antecedentFamilial);
}
