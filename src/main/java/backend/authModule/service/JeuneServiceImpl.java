package backend.authModule.service;

import backend.authModule.dto.JeuneDTO;
import backend.authModule.dto.JeuneNonScolariseDto;
import backend.authModule.dto.JeuneScolariseDto;
import backend.authModule.entities.*;
import backend.authModule.exception.EmailNonValideException;
import backend.authModule.exception.JeuneException;
import backend.authModule.exception.JeuneNotFoundException;
import backend.authModule.exception.PhoneNonValideException;
import backend.authModule.mappers.JeuneMapper;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@AllArgsConstructor
//il faut ajouté de global hundler Exception
public class JeuneServiceImpl implements JeuneService {

    private static final long EXPIRATION_TIME_MS = 60 * 60 * 1000;

    private JeuneMapper jeuneMapper;
    private Validatore validatore;
    private JeuneRepository jeuneRepository;

    private AntecedentFamilialRepository antecedentFamilialRepository;
    private AntecedentPersonnelRepository antecedentPersonnelRepository;

    private JavaMailSender mailSender;
    private PasswordEncoder passwordEncoder;

    private ConfirmationTokenRepository confirmationTokenRepository;
    @Override
    public JeuneDTO saveJeune(Jeune jeune) throws EmailNonValideException, PhoneNonValideException {

        AppUser appUser=jeune.getAppUser();
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        if(!validatore.isValidEmail(jeune.getAppUser().getMail())){
            throw new EmailNonValideException("Invalid email format");
        }if(!validatore.isValidMoroccanPhoneNumber(jeune.getAppUser().getNumTele())){
            throw new PhoneNonValideException("Invalid phone number format");
        }

        LocalDate birthDate = jeune.getDateNaissance().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        jeune.setAge(age);

        int identifiant_patient = new Random().nextInt(900000) + 100000;

        jeune.setIdentifiantPatient(identifiant_patient);

        Jeune savedJeune = jeuneRepository.save(jeune);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, savedJeune);
        confirmationTokenRepository.save(confirmationToken);

        new Thread(() -> sendConfirmationEmail(savedJeune.getAppUser().getMail(), token)).start();

        JeuneDTO jeuneDTO=jeuneMapper.fromJeune(savedJeune);
        return jeuneDTO;
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


    public Object getJeuneById(Long id) throws JeuneNotFoundException {


        Jeune jeune= jeuneRepository.findById(id).orElseThrow(() -> new JeuneNotFoundException("Jeune not found for this id :: " + id));
        if(jeune.isScolarise()){
            JeuneScolarise scolarise=(JeuneScolarise) jeune;
            return jeuneMapper.fromJeuneScolarise(scolarise);
        }else {
            JeuneNonScolarise nonScolarise =(JeuneNonScolarise) jeune;
            return  jeuneMapper.fromJeuneNonScolarise(nonScolarise);
        }
    }




    @Override
    public void sendEmail(String to, String subject, String htmlBody) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true indique que le contenu est HTML
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    @Override
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

    @Override
    public void sendConfirmationEmail(String to, String token) {
        String confirmationUrl = "http://localhost:8080/register/jeunes/confirmation?token=" + token;
        String subject = "Email Confirmation";
        String htmlBody = "<p>Please confirm your email by clicking the following link:</p>"
                + "<p><a href=\"" + confirmationUrl + "\">Confirm Email</a></p>";

        sendEmail(to, subject, htmlBody);
    }




}
