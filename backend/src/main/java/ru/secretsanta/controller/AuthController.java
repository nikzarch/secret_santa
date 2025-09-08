package ru.secretsanta.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.secretsanta.dto.request.LoginRequest;
import ru.secretsanta.dto.request.RegisterRequest;
import ru.secretsanta.dto.response.AuthResponse;
import ru.secretsanta.dto.response.ErrorResponse;
import ru.secretsanta.dto.response.RegisterViaTokenResponse;
import ru.secretsanta.service.AuthService;
import ru.secretsanta.service.InviteService;

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

    @PostMapping("/register")
    public ResponseEntity registerViaToken(@RequestParam String token,
                                           @RequestParam String password) {
        return inviteService.validateToken(token)
                .map(invite -> {
                    authService.register(new RegisterRequest(invite.getUsername(), password));
                    inviteService.markUsed(invite);

                    return ResponseEntity.ok(
                            new RegisterViaTokenResponse("user successfully registered",invite.getUsername())
                    );
                })
                .orElse(new ResponseEntity(new ErrorResponse("token has expired"), HttpStatus.GONE));
    }

}
