package backend.authModule.exception;

public class AgeNonValideException extends RuntimeException{
    public AgeNonValideException(String message){
        super(message);
    }
}
