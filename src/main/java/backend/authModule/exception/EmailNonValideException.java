package backend.authModule.exception;

public class EmailNonValideException extends RuntimeException{
    public EmailNonValideException(String message){
        super(message);
    }
}
