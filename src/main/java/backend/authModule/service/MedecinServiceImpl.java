package backend.authModule.service;

import backend.authModule.entities.AppUser;
import backend.authModule.entities.ConfirmationToken;
import backend.authModule.entities.Medecin;
import backend.authModule.exception.MedecinException;
import backend.authModule.repository.ConfirmationTokenRepository;
import backend.authModule.repository.MedecinRepository;
import backend.authModule.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class MedecinServiceImpl implements MedecinService {


    private static final long EXPIRATION_TIME_MS = 60 * 60 * 1000;

    private MedecinRepository medecinRepository;
    private UserRepository userRepository;

    private ConfirmationTokenRepository confirmationTokenRepository;
    private JavaMailSender mailSender;


    @Override
    public Medecin saveMecine(Medecin medecin) throws MedecinException {

        try {
            AppUser appUser = new AppUser();
            appUser.setNom(medecin.getAppUser().getNom());
            appUser.setMail(medecin.getAppUser().getMail());
            appUser.setPrenom(medecin.getAppUser().getPrenom());
            appUser.setPassword(medecin.getAppUser().getPassword());
            appUser.setNumTele(medecin.getAppUser().getNumTele());
            AppUser savedAppUser = userRepository.save(appUser);
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            System.out.println(savedAppUser);
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            medecin.setAppUser(savedAppUser);
            Medecin savedMedecin = medecinRepository.save(medecin);

            String token = UUID.randomUUID().toString();
            ConfirmationToken confirmationToken = new ConfirmationToken();
            confirmationToken.setMedecin(savedMedecin);
            confirmationToken.setToken(token);
            confirmationTokenRepository.save(confirmationToken);

            sendConfirmationEmail(savedMedecin.getAppUser().getMail(),token);
            return savedMedecin;
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException cause = (ConstraintViolationException) e.getCause();
                String constraintName = cause.getConstraintName();
                if (constraintName.contains("mail")) {
                    throw new MedecinException("L'email spécifié est déjà utilisé par un autre utilisateur");
                } else if (constraintName.contains("cin")) {
                    throw new MedecinException("Le numéro de CIN spécifié est déjà utilisé par un autre utilisateur");
                }
            }
            throw new MedecinException("Une erreur s'est produite lors de l'enregistrement du médecin", e);
        }
    }

    public void sendConfirmationEmail(String to, String token) {
        String confirmationUrl = "http://localhost:8080/medecins/confirmation?token=" + token;
        String subject = "Email Confirmation";
        String htmlBody = "<p>Please confirm your email by clicking the following link:</p>"
                + "<p><a href=\"" + confirmationUrl + "\">Confirm Email</a></p>";

        sendEmail(to, subject, htmlBody);
    }
    public Medecin confirmEmail(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token);
        if (confirmationToken != null) {
            Date now = new Date();
            Medecin medecin = confirmationToken.getMedecin();
            long diffMs = now.getTime() - confirmationToken.getCreatedDate().getTime();
            if (diffMs > EXPIRATION_TIME_MS) {
                throw new RuntimeException("Confirmation token has expired");
            }
            medecin.setConfirmed(true);
            medecinRepository.save(medecin);
            return medecin;
        } else {
            throw new RuntimeException("Invalid confirmation token");
        }
    }
    private void sendEmail(String to, String subject, String htmlBody) {
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

}
