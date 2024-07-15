package backend.authModule.exception;

public class UsernameNotFoundException extends Exception{
    public UsernameNotFoundException(String message){
        super(message);
    }

    public UsernameNotFoundException(String message,Throwable throwable){
        super(message,throwable);
    }
}
