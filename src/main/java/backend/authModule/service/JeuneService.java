package backend.authModule.service;

import backend.authModule.dto.JeuneDTO;
import backend.authModule.entities.*;
import backend.authModule.exception.EmailNonValideException;
import backend.authModule.exception.JeuneException;
import backend.authModule.exception.PhoneNonValideException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


public interface JeuneService extends ConfirmeMailService<Jeune> {

    JeuneDTO saveJeune(Jeune jeune) throws EmailNonValideException, PhoneNonValideException;
    AntecedentFamilial addAntecedentFamilial(Long jeuneId, AntecedentFamilial antecedentFamilial);
    AntecedentPersonnel addAntecedentPersonnel(Long jeuneId, AntecedentPersonnel antecedentPersonnel);
    Map<String, Object> getAntecedents(Long jeuneId) throws JeuneException;
}
