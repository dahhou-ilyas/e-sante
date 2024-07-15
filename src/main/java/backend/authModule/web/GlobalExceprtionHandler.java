package backend.authModule.web;

import backend.authModule.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceprtionHandler {


    @ExceptionHandler(EmailNonValideException.class)
    public ResponseEntity<String> handleBankAccountNotFoundException(EmailNonValideException exception){
        return new ResponseEntity<>("Invalid email format", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AgeNonValideException.class)
    public ResponseEntity<String> handleBankAccountNotFoundException(AgeNonValideException exception){
        return new ResponseEntity<>("invalid age it must be between 10 and 30 years old", HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(InvalidResetPasswordTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidResetPasswordTokenException(InvalidResetPasswordTokenException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse("Token de réinitialisation de mot de passe invalide", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(JeuneException.class)
    public ResponseEntity<ErrorResponse> handleJeuneException(JeuneException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse("Erreur Jeune", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JeuneNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleJeuneNotFoundException(JeuneNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse("Jeune non trouvé", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MedecinException.class)
    public ResponseEntity<ErrorResponse> handleMedecinException(MedecinException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse("Erreur Médecin", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MedecinNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMedecinNotFoundException(MedecinNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse("Médecin non trouvé", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PhoneNonValideException.class)
    public ResponseEntity<ErrorResponse> handlePhoneNonValideException(PhoneNonValideException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse("Numéro de téléphone non valide", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProfessionnelSanteException.class)
    public ResponseEntity<ErrorResponse> handleProfessionnelSanteException(ProfessionnelSanteException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse("Erreur Professionnel de Santé", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse("Erreur serveur", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    static class ErrorResponse {
        private String error;
        private String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }

        // Getters et setters
        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
