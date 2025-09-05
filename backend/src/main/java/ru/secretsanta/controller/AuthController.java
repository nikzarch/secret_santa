package ru.secretsanta.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.secretsanta.dto.request.LoginRequest;
import ru.secretsanta.dto.request.RegisterRequest;
import ru.secretsanta.dto.response.AuthResponse;
import ru.secretsanta.dto.response.ErrorResponse;
import ru.secretsanta.entity.Role;
import ru.secretsanta.entity.User;
import ru.secretsanta.exception.InvalidCredentialsException;
import ru.secretsanta.exception.UserNotFoundException;
import ru.secretsanta.repository.UserRepository;
import ru.secretsanta.service.AuthService;
import ru.secretsanta.service.InviteService;
import ru.secretsanta.util.JWTUtil;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final InviteService inviteService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        AuthResponse authResponse = authService.login(request);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register1")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        AuthResponse authResponse = authService.register(request);

        return ResponseEntity.ok(authResponse);

    }
    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> registerViaToken(@RequestParam String token,
                                      @RequestParam String password) {
        return inviteService.validateToken(token)
                .map(invite -> {
                    authService.register(new RegisterRequest(invite.getUsername(), password));
                    inviteService.markUsed(invite);
                    return ResponseEntity.ok(Map.of("message", "User registered"));
                })
                .orElse(ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired token")));
    }

}
