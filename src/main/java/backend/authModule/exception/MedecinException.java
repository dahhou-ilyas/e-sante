package backend.authModule.exception;

public class MedecinException extends Exception{
    public MedecinException(String message) {
        super(message);
    }

    public MedecinException(String message, Throwable cause) {
        super(message, cause);
    }
}
