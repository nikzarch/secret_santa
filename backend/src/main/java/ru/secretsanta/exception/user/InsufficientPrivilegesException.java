package ru.secretsanta.exception.user;

public class InsufficientPrivilegesException extends RuntimeException{
    public InsufficientPrivilegesException(String message){
        super(message);
    }
}
