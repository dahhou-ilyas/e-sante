package backend.authModule.service;

import backend.authModule.dto.MedecinResponseDTO;
import backend.authModule.entities.AppUser;
import backend.authModule.entities.ConfirmationToken;
import backend.authModule.entities.Medecin;
import backend.authModule.exception.MedecinException;
import backend.authModule.exception.MedecinNotFoundException;
import backend.authModule.mappers.MedecineMapper;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class MedecinServiceImpl implements MedecinService {


    private static final long EXPIRATION_TIME_MS = 60 * 60 * 1000;

    private MedecinRepository medecinRepository;
    private UserRepository userRepository;
    private MedecineMapper medecineMapper;

    private ConfirmationTokenRepository confirmationTokenRepository;
    private JavaMailSender mailSender;

    private PasswordEncoder passwordEncoder;

    @Override
    public MedecinResponseDTO saveMecine(Medecin medecin) throws MedecinException {

        try {
            if(medecinRepository.findByMail(medecin.getAppUser().getMail()).isPresent()){
                throw new MedecinException("mail est déja existe");
            }

            AppUser appUser=medecin.getAppUser();
            appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
            Medecin savedMedecin = medecinRepository.save(medecin);

            String token = UUID.randomUUID().toString();
            ConfirmationToken confirmationToken = new ConfirmationToken();
            confirmationToken.setMedecin(savedMedecin);
            confirmationToken.setCreatedDate(new Date());
            confirmationToken.setToken(token);
            confirmationTokenRepository.save(confirmationToken);

            new Thread(() -> sendConfirmationEmail(savedMedecin.getAppUser().getMail(),token)).start();

            MedecinResponseDTO medecinResponseDTO = medecineMapper.fromMedcine(savedMedecin);
            return medecinResponseDTO;
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

    public MedecinResponseDTO getMedecinById(Long id) throws MedecinNotFoundException {
        Optional<Medecin> medecinOptional = medecinRepository.findById(id);
        if (medecinOptional.isEmpty()) {
            throw new MedecinNotFoundException("Médecin non trouvé avec l'ID : " + id);
        }
        MedecinResponseDTO medecinResponseDTO=medecineMapper.fromMedcine(medecinOptional.get());
        return medecinResponseDTO;
    }

    public MedecinResponseDTO updateMedecinPartial(Long id, Map<String, Object> updates) throws MedecinNotFoundException {
        Medecin existingMedecin = medecinRepository.findById(id)
                .orElseThrow(() -> new MedecinNotFoundException("Medecin not found with id " + id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "nom":
                    existingMedecin.getAppUser().setNom((String) value);
                    break;
                case "prenom":
                    existingMedecin.getAppUser().setPrenom((String) value);
                    break;
                case "mail":
                    existingMedecin.getAppUser().setMail((String) value);
                    break;
                case "numTele":
                    existingMedecin.getAppUser().setNumTele((String) value);
                    break;
                case "password":
                    existingMedecin.getAppUser().setPassword((String) value);
                    break;
                case "cin":
                    existingMedecin.setCin((String) value);
                    break;
                case "inpe":
                    existingMedecin.setInpe((String) value);
                    break;
                case "ppr":
                    existingMedecin.setPpr((String) value);
                    break;
                case "estMedcinESJ":
                    existingMedecin.setEstMedcinESJ((Boolean) value);
                    break;
                case "estGeneraliste":
                    existingMedecin.setEstGeneraliste((Boolean) value);
                    break;
                case "specialite":
                    existingMedecin.setSpecialite((String) value);
                    break;
                case "confirmed":
                    existingMedecin.setConfirmed((Boolean) value);
                    break;
                case "isFirstAuth":
                    existingMedecin.setIsFirstAuth((Boolean) value);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid attribute: " + key);
            }
        });

        userRepository.save(existingMedecin.getAppUser());
        medecinRepository.save(existingMedecin);

        return medecineMapper.fromMedcine(existingMedecin);
    }

    @Override
    public List<MedecinResponseDTO> getAllMedecins() {
        List<Medecin> medecins=medecinRepository.findAll();
        return medecins.stream().map(m->medecineMapper.fromMedcine(m)).collect(Collectors.toList());
    }

    @Override
    public void deleteMedecin(Long id) throws MedecinNotFoundException, MedecinException {
        Optional<Medecin> medecinOptional = medecinRepository.findById(id);
        if (medecinOptional.isPresent()) {
            try {
                medecinRepository.delete(medecinOptional.get());
            } catch (Exception e) {
                throw new MedecinException("Une erreur s'est produite lors de la suppression du médecin", e);
            }
        } else {
            throw new MedecinNotFoundException("Médecin non trouvé avec l'ID : " + id);
        }
    }


    @Override
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

    public void sendConfirmationEmail(String to, String token) {
        String confirmationUrl = "http://localhost:8080/register/medecins/confirmation?token=" + token;
        String subject = "Email Confirmation";
        String htmlBody = "<p>Please confirm your email by clicking the following link:</p>"
                + "<p><a href=\"" + confirmationUrl + "\">Confirm Email</a></p>";

        sendEmail(to, subject, htmlBody);
    }

    public void resendToken(String email) throws MedecinException {
        Medecin medecin = medecinRepository.findByMail(email)
                .orElseThrow(() -> new MedecinException("Medecin not found"));

        // Supprimer l'ancien token s'il existe
        ConfirmationToken existingToken = confirmationTokenRepository.findByMedecin(medecin);
        System.out.println("**********************");
        System.out.println(existingToken);
        System.out.println("**********************");
        if (existingToken != null) {
            confirmationTokenRepository.delete(existingToken);
            confirmationTokenRepository.flush();
        }

        // Créer et sauvegarder un nouveau token
        ConfirmationToken newToken = new ConfirmationToken();
        newToken.setMedecin(medecin);
        newToken.setCreatedDate(new Date());
        newToken.setToken(UUID.randomUUID().toString());
        confirmationTokenRepository.save(newToken);

        // Envoyer l'email de confirmation avec le nouveau token
        sendConfirmationEmail(medecin.getAppUser().getMail(), newToken.getToken());
    }

}
