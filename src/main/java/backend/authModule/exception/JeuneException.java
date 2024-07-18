package backend.authModule.exception;

public class JeuneException extends RuntimeException{

    public JeuneException(String message){
        super(message);
    }

    public JeuneException(String message,Throwable throwable){
        super(message,throwable);
    }
}
