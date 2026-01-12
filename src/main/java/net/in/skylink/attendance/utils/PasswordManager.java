package net.in.skylink.attendance.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordManager {
    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordManager(@Value("${passwordStrength:15}") int passwordStrength) {
        this.passwordEncoder = new BCryptPasswordEncoder(passwordStrength);
    }

    public BCryptPasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public String generateEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}