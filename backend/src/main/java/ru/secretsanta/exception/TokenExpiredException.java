package ru.secretsanta.exception;

public class TokenExpiredException extends RuntimeException{

    public TokenExpiredException(String message){
        super(message);
    }
}
