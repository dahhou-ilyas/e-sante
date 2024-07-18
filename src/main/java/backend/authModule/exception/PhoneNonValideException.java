package backend.authModule.exception;

public class PhoneNonValideException extends RuntimeException{
    public PhoneNonValideException(String message){
        super(message);
    }
}
