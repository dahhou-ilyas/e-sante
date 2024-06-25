package backend.authModule.exception;

public class ProfessionnelSanteException extends Exception{
    public ProfessionnelSanteException(String message){
        super(message);
    }

    public ProfessionnelSanteException(String message,Throwable throwable){
        super(message,throwable);
    }
}
