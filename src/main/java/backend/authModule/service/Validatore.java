package backend.authModule.service;

public interface Validatore {
    boolean isValidEmail(String email);
    boolean isValidMoroccanPhoneNumber(String phoneNumber);
    boolean isValidCNIE(String cin);
}
