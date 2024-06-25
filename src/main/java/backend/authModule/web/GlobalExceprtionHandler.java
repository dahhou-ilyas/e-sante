package backend.authModule.web;

import backend.authModule.exception.AgeNonValideException;
import backend.authModule.exception.EmailNonValideException;
import backend.authModule.exception.PhoneNonValideException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceprtionHandler {

    @ExceptionHandler(PhoneNonValideException.class)
    public ResponseEntity<String> handleBankAccountNotFoundException(PhoneNonValideException exception){
        return new ResponseEntity<>("Invalid phone number format", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailNonValideException.class)
    public ResponseEntity<String> handleBankAccountNotFoundException(EmailNonValideException exception){
        return new ResponseEntity<>("Invalid email format", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AgeNonValideException.class)
    public ResponseEntity<String> handleBankAccountNotFoundException(AgeNonValideException exception){
        return new ResponseEntity<>("invalid age it must be between 10 and 30 years old", HttpStatus.BAD_REQUEST);
    }

}
