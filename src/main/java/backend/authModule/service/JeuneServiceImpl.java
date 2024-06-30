package backend.authModule.service;

import backend.authModule.entities.AntecedentFamilial;
import backend.authModule.entities.AntecedentPersonnel;
import backend.authModule.entities.ConfirmationToken;
import backend.authModule.entities.Jeune;
import backend.authModule.exception.EmailNonValideException;
import backend.authModule.exception.JeuneException;
import backend.authModule.exception.PhoneNonValideException;
import backend.authModule.repository.AntecedentFamilialRepository;
import backend.authModule.repository.AntecedentPersonnelRepository;
import backend.authModule.repository.ConfirmationTokenRepository;
import backend.authModule.repository.JeuneRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    private JavaMailSender mailSender;

    private ConfirmationTokenRepository confirmationTokenRepository;
    @Override
    public Jeune saveJeune(Jeune jeune) throws EmailNonValideException, PhoneNonValideException {
        if(!isValidEmail(jeune.getMail())){
            throw new EmailNonValideException("Invalid email format");
        }if(!isValidMoroccanPhoneNumber(jeune.getNumTele())){
            throw new PhoneNonValideException("Invalid phone number format");
        }

        int identifiant_patient = new Random().nextInt(900000) + 100000;

        jeune.setIdentifiantPatient(identifiant_patient);

        Jeune savedJeune = jeuneRepository.save(jeune);


        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, savedJeune);
        confirmationTokenRepository.save(confirmationToken);


        sendConfirmationEmail(savedJeune.getMail(), token);

        return savedJeune;
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

    public Map<String,Object> getAntecedents(Long jeuneId) throws JeuneException {
        Optional<Jeune> saveJeune= jeuneRepository.findById(jeuneId);

        if (saveJeune.isPresent()) {
            Jeune jeune = saveJeune.get();
            AntecedentFamilial antecedentFamilial = antecedentFamilialRepository.findByJeune(jeune)
                    .orElseThrow(() -> new JeuneException("Antécédent familial non trouvé pour le jeune"));
            AntecedentPersonnel antecedentPersonnel = antecedentPersonnelRepository.findByJeune(jeune)
                    .orElseThrow(() -> new JeuneException("Antécédent personnel non trouvé pour le jeune"));
            Map<String, Object> result = new HashMap<>();
            result.put("AntecedentFamilial", antecedentFamilial);
            result.put("AntecedentPersonnel", antecedentPersonnel);
            return result;
        } else {
            throw new JeuneException("Jeune n'existe pas");
        }
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

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendConfirmationEmail(String to, String token) {
        String confirmationUrl = "http://localhost:8080/jeunes/confirmation?token=" + token;
        String subject = "Email Confirmation";
        String body = "Please confirm your email by clicking the following link: " + confirmationUrl;
        sendEmail(to, subject, body);
    }

    public Jeune confirmEmail(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token);
        if (confirmationToken != null) {
            Jeune jeune = confirmationToken.getJeune();
            jeune.setIsConfirmed(true);
            jeuneRepository.save(jeune);
            return jeune;
        } else {
            throw new RuntimeException("Invalid confirmation token");
        }
    }
}
