package ru.secretsanta.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.secretsanta.dto.request.LoginRequest;
import ru.secretsanta.dto.response.AuthResponse;
import ru.secretsanta.dto.response.ErrorResponse;
import ru.secretsanta.dto.response.RegisterViaTokenResponse;
import ru.secretsanta.exception.user.TokenExpiredException;
import ru.secretsanta.service.user.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        AuthResponse authResponse = authService.login(request);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerViaToken(@RequestParam String token,
                                              @RequestParam String password) {
        try {
            RegisterViaTokenResponse response = authService.registerViaToken(token, password);
            return ResponseEntity.ok(response);
        } catch (TokenExpiredException exc) {
            return ResponseEntity.status(HttpStatus.GONE)
                    .body(new ErrorResponse("token has expired"));
        }
    }

}
