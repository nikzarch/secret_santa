package ru.secretsanta.service;

import ru.secretsanta.dto.request.LoginRequest;
import ru.secretsanta.dto.request.RegisterRequest;
import ru.secretsanta.dto.response.AuthResponse;
import ru.secretsanta.dto.response.RegisterViaTokenResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    RegisterViaTokenResponse registerViaToken(String token, String password);
}
