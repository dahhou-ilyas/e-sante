package backend.authModule.service;

import backend.authModule.entities.AntecedentFamilial;
import backend.authModule.entities.AntecedentPersonnel;
import backend.authModule.entities.Jeune;
import backend.authModule.entities.JeuneScolarise;
import backend.authModule.exception.EmailNonValideException;
import backend.authModule.exception.JeuneException;
import backend.authModule.exception.PhoneNonValideException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


public interface JeuneService extends ConfirmeMailService<Jeune> {
    JeuneScolarise saveJeuneScolarise(JeuneScolarise jeuneScolarise);
    Jeune saveJeune(Jeune jeune) throws EmailNonValideException, PhoneNonValideException;
    AntecedentFamilial addAntecedentFamilial(Long jeuneId, AntecedentFamilial antecedentFamilial);
    AntecedentPersonnel addAntecedentPersonnel(Long jeuneId, AntecedentPersonnel antecedentPersonnel);
    Map<String, Object> getAntecedents(Long jeuneId) throws JeuneException;
    void sendEmail(String to, String subject, String body);
    void sendConfirmationEmail(String to, String token);
    Jeune confirmEmail(String token);
}
