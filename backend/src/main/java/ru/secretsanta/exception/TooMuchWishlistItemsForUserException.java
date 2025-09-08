package ru.secretsanta.exception;

public class TooMuchWishlistItemsForUserException extends RuntimeException{
    public TooMuchWishlistItemsForUserException(String message){
        super(message);
    }
}
