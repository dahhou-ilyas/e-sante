package backend.authModule.service;

import backend.authModule.exception.InvalidResetPasswordTokenException;
import backend.authModule.exception.UserNotFoundException;

public interface PasswordResetService {
    void initiatePasswordReset(String email) throws UserNotFoundException;

    boolean validatePasswordResetToken(String token);

    void resetPassword(String token, String newPassword) throws InvalidResetPasswordTokenException;

}
