package backend.authModule.exception;

public class JeuneException extends Exception{

    public JeuneException(String message){
        super(message);
    }

    public JeuneException(String message,Throwable throwable){
        super(message,throwable);
    }
}
