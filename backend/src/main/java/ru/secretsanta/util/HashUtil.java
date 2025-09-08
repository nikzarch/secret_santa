package ru.secretsanta.util;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component
public class HashUtil {

    private static final int SALT_LENGTH = 16;

    public byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    public HashResult hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] salt = generateSalt();
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            return new HashResult(bytesToHex(hashedPassword),salt);
        } catch (NoSuchAlgorithmException exc) {
            throw new RuntimeException("Ошибка хэширования пароля", exc);
        }
    }
    public HashResult hashPassword(String password, byte[] salt){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            return new HashResult(bytesToHex(hashedPassword),salt);
        }catch (NoSuchAlgorithmException exc) {
            throw new RuntimeException("Ошибка хэширования пароля", exc);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
