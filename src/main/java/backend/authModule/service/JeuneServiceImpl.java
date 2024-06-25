package backend.authModule.service;

import backend.authModule.entities.AntecedentFamilial;
import backend.authModule.entities.AntecedentPersonnel;
import backend.authModule.entities.Jeune;
import backend.authModule.exception.EmailNonValideException;
import backend.authModule.exception.PhoneNonValideException;
import backend.authModule.repository.AntecedentFamilialRepository;
import backend.authModule.repository.AntecedentPersonnelRepository;
import backend.authModule.repository.JeuneRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@AllArgsConstructor
//il faut ajouté de global hundler Exception
public class JeuneServiceImpl implements JeuneService {

    private JeuneRepository jeuneRepository;

    private AntecedentFamilialRepository antecedentFamilialRepository;
    private AntecedentPersonnelRepository antecedentPersonnelRepository;
    @Override
    public Jeune saveJeune(Jeune jeune) throws EmailNonValideException, PhoneNonValideException {
        if(!isValidEmail(jeune.getMail())){
            throw new EmailNonValideException("Invalid email format");
        }if(!isValidMoroccanPhoneNumber(jeune.getNumTele())){
            throw new PhoneNonValideException("Invalid phone number format");
        }
        return jeuneRepository.save(jeune);
    }

    @Override
    public AntecedentFamilial addAntecedentFamilial(Long jeuneId, AntecedentFamilial antecedentFamilial) {
        Jeune jeune = jeuneRepository.findById(jeuneId).orElseThrow(() -> new IllegalArgumentException("Jeune not found"));
        antecedentFamilial.setJeune(jeune);
        return antecedentFamilialRepository.save(antecedentFamilial);
    }

    public AntecedentPersonnel addAntecedentPersonnel(Long jeuneId, AntecedentPersonnel antecedentPersonnel) {
        Jeune jeune = jeuneRepository.findById(jeuneId).orElseThrow(() -> new IllegalArgumentException("Jeune not found"));
        antecedentPersonnel.setJeune(jeune);
        return antecedentPersonnelRepository.save(antecedentPersonnel);
    }


    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidMoroccanPhoneNumber(String phoneNumber) {
        String phoneRegex = "^(\\+212|0)([5-7])\\d{8}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        if (phoneNumber == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
