package backend.authModule.service;


import backend.authModule.entities.AppUser;
import backend.authModule.entities.PasswordResetToken;
import backend.authModule.exception.InvalidResetPasswordTokenException;
import backend.authModule.exception.UserNotFoundException;
import backend.authModule.repository.PasswordResetTokenRepository;
import backend.authModule.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private UserRepository userRepository;

    private PasswordResetTokenRepository tokenRepository;

    private JavaMailSender mailSender;

    @Override
    public void initiatePasswordReset(String email) throws UserNotFoundException {
        Optional<AppUser> userOptional = userRepository.findByMail(email);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("User not found");
        }
        AppUser user = userOptional.get();
        String token = UUID.randomUUID().toString();

        Date expiryDate = new Date(System.currentTimeMillis() + 3600 * 1000); // 1 hour expiry
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, expiryDate);
        tokenRepository.save(passwordResetToken);

        sendPasswordResetEmail(user.getMail(), token);
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);

        return resetToken != null && resetToken.getExpiryDate().after(new Date());
    }

    @Transactional
    @Override
    public void resetPassword(String token, String newPassword) throws InvalidResetPasswordTokenException {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null || resetToken.getExpiryDate().before(new Date())) {
            throw new InvalidResetPasswordTokenException("Invalid or expired token");
        }
        AppUser user = resetToken.getUser();
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    private void sendPasswordResetEmail(String recipientEmail, String token){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject("Réinitialisation de mot de passe");
        mailMessage.setText("Pour réinitialiser votre mot de passe, utilisé le token suviant  : "+token);

        mailSender.send(mailMessage);

    }
}
