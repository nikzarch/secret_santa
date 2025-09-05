package ru.secretsanta.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.secretsanta.dto.response.UserShortResponse;
import ru.secretsanta.entity.User;
import ru.secretsanta.mapper.UserMapper;
import ru.secretsanta.repository.UserRepository;


@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<UserShortResponse> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new RuntimeException("User is not authenticated");
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        User user = userRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(new UserShortResponse(
                user.getId(),
                user.getName(),
                user.getRole().name()
        ));
    }
}
