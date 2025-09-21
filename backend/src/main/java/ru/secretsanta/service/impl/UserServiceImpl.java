package ru.secretsanta.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.secretsanta.entity.User;
import ru.secretsanta.exception.UserNotFoundException;
import ru.secretsanta.repository.UserRepository;
import ru.secretsanta.service.UserService;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getUserByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteUserByName(String name) {
        userRepository.delete(userRepository.findByName(name).orElseThrow(() -> new UserNotFoundException("user not found")));
    }
}
