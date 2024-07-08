package backend.authModule.service;

import backend.authModule.entities.*;
import backend.authModule.exception.EmailNonValideException;
import backend.authModule.exception.JeuneException;
import backend.authModule.exception.PhoneNonValideException;
import backend.authModule.repository.AntecedentFamilialRepository;
import backend.authModule.repository.AntecedentPersonnelRepository;
import backend.authModule.repository.ConfirmationTokenRepository;
import backend.authModule.repository.JeuneRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    private static final long EXPIRATION_TIME_MS = 60 * 60 * 1000;

    private Validatore validatore;
    private JeuneRepository jeuneRepository;

    private AntecedentFamilialRepository antecedentFamilialRepository;
    private AntecedentPersonnelRepository antecedentPersonnelRepository;

    private JavaMailSender mailSender;

    private ConfirmationTokenRepository confirmationTokenRepository;
    @Override
    public Jeune saveJeune(Jeune jeune) throws EmailNonValideException, PhoneNonValideException {
        /*
        if(!validatore.isValidEmail(jeune.getMail())){
            throw new EmailNonValideException("Invalid email format");
        }if(!validatore.isValidMoroccanPhoneNumber(jeune.getNumTele())){
            throw new PhoneNonValideException("Invalid phone number format");
        }

        int identifiant_patient = new Random().nextInt(900000) + 100000;

        jeune.setIdentifiantPatient(identifiant_patient);

        Jeune savedJeune = jeuneRepository.save(jeune);


        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, savedJeune);
        confirmationTokenRepository.save(confirmationToken);


        sendConfirmationEmail(savedJeune.getMail(), token);


         */
        return null;
    }

    public JeuneScolarise saveJeuneScolarise(JeuneScolarise jeuneScolarise) {
        // Ajouter toute logique de validation ou de traitement avant d'enregistrer
        return jeuneRepository.save(jeuneScolarise);
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

    public void sendEmail(String to, String subject, String htmlBody) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true indique que le contenu est HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            // Gérer les exceptions liées à l'envoi de l'email
            e.printStackTrace();
            // Vous pouvez lancer une exception spécifique ou gérer l'erreur d'une autre manière
        }
    }

    public void sendConfirmationEmail(String to, String token) {
        String confirmationUrl = "http://localhost:8080/medecins/confirmation?token=" + token;
        String subject = "Email Confirmation";
        String htmlBody = "<p>Please confirm your email by clicking the following link:</p>"
                + "<p><a href=\"" + confirmationUrl + "\">Confirm Email</a></p>";

        sendEmail(to, subject, htmlBody);
    }

    public Jeune confirmEmail(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token);
        if (confirmationToken != null) {
            Date now = new Date();
            Jeune jeune = confirmationToken.getJeune();
            long diffMs = now.getTime() - confirmationToken.getCreatedDate().getTime();
            if (diffMs > EXPIRATION_TIME_MS) {
                throw new RuntimeException("Confirmation token has expired");
            }
            jeune.setIsConfirmed(true);
            jeuneRepository.save(jeune);
            return jeune;
        } else {
            throw new RuntimeException("Invalid confirmation token");
        }
    }
}
