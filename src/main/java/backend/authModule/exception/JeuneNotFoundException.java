package backend.authModule.exception;

public class JeuneNotFoundException extends RuntimeException{
    public JeuneNotFoundException(String message){
        super(message);
    }

    public JeuneNotFoundException(String message,Throwable throwable){
        super(message,throwable);
    }
}
