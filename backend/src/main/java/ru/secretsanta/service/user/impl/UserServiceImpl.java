package ru.secretsanta.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.secretsanta.dto.response.UserShortResponse;
import ru.secretsanta.entity.user.User;
import ru.secretsanta.exception.common.NotFoundException;
import ru.secretsanta.mapper.user.UserMapper;
import ru.secretsanta.repository.UserRepository;
import ru.secretsanta.service.user.UserService;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserShortResponse getUserByName(String name) {
        return UserMapper.toUserShortResponse(userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    @Override
    public void deleteUserByName(String name) {
        userRepository.delete(userRepository.findByName(name).orElseThrow(() -> new NotFoundException("user not found")));
    }

    @Override
    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new RuntimeException("User is not authenticated");
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        return userRepository.findByName(username).orElseThrow(()->  new NotFoundException("user not found"));
    }

    @Override
    public Integer getUsersRegistered(){
        return (int) userRepository.count();
    }
}
