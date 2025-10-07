package ru.secretsanta.exception.common;

public class TooMuchItemsException extends RuntimeException{
    public TooMuchItemsException(String message){
        super(message);
    }
}
