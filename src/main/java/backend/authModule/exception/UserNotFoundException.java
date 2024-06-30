package backend.authModule.exception;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String message){
        super(message);
    }

    public UserNotFoundException(String message,Throwable throwable){
        super(message,throwable);
    }
}
