package ru.secretsanta.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.secretsanta.dto.request.LoginRequest;
import ru.secretsanta.dto.request.RegisterRequest;
import ru.secretsanta.dto.response.AuthResponse;
import ru.secretsanta.entity.Role;
import ru.secretsanta.entity.User;
import ru.secretsanta.exception.InvalidCredentialsException;
import ru.secretsanta.exception.UserNotFoundException;
import ru.secretsanta.repository.UserRepository;
import ru.secretsanta.service.AuthService;
import ru.secretsanta.util.HashUtil;
import ru.secretsanta.util.JWTUtil;

import java.util.Base64;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;

    private final HashUtil hashUtil;

    private final JWTUtil jwtUtil;


    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        byte[] salt = hashUtil.generateSalt();
        String hashedPassword = hashUtil.hashPassword(registerRequest.password(), salt);
        User user = User.builder()
                .name(registerRequest.name())
                .passwordHash(hashedPassword)
                .salt(Base64.getEncoder().encodeToString(salt))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getName(), Role.USER.toString());
        return new AuthResponse(token);
    }


    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByName(loginRequest.name());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        User user = optionalUser.get();

        byte[] salt = Base64.getDecoder().decode(user.getSalt());

        String hashedInputPassword = hashUtil.hashPassword(loginRequest.password(), salt);

        if (!hashedInputPassword.equals(user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getName(), user.getRole().toString());
        return new AuthResponse(token);
    }

    @Override
    public boolean isAdminByToken(String token) {
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByName(username).get();
        return user.getRole() == Role.ADMIN;
    }
    @Override
    public String getNameByToken(String token){
        return jwtUtil.extractUsername(token);
    }
}
