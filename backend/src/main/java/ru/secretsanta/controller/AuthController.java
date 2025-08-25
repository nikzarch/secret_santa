package ru.secretsanta.controller;

import jakarta.validation.Valid;
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
import ru.secretsanta.util.JWTUtil;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final JWTUtil jwtUtil;

    private final UserRepository userRepository;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        AuthResponse authResponse = authService.login(request);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        AuthResponse authResponse = authService.register(request);

        return ResponseEntity.ok(authResponse);

    }


}
