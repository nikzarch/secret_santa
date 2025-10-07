package ru.secretsanta.exceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.secretsanta.dto.response.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.secretsanta.exception.common.NotFoundException;
import ru.secretsanta.exception.common.TooMuchItemsException;
import ru.secretsanta.exception.user.InviteStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException exc) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", exc.getMessage());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        exc.printStackTrace();
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(NotFoundException exc) {
        return new ResponseEntity<>(Map.of("error", "Requested item not found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TooMuchItemsException.class)
    public ResponseEntity<ErrorResponse> handleTooMuchWishlistItemsForUserException(TooMuchItemsException exc){
        return new ResponseEntity<>(new ErrorResponse(exc.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InviteStatusException.class)
    public ResponseEntity<ErrorResponse> handleInviteStatusException(InviteStatusException exc){
        return new ResponseEntity<>(new ErrorResponse(exc.getMessage()),HttpStatus.BAD_REQUEST);
    }
}