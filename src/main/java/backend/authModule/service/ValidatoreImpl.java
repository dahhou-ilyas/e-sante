package backend.authModule.service;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidatoreImpl implements Validatore {
    @Override
    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public boolean isValidMoroccanPhoneNumber(String phoneNumber) {
        String phoneRegex = "^(\\+212|0)([5-7])\\d{8}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        if (phoneNumber == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public boolean isValidCNIE(String cin) {
        String regex = "^[A-Z]{1,2}[0-9]{6}$";

        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(cin).matches();
    }

}
