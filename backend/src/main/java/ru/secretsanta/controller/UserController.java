package ru.secretsanta.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.secretsanta.dto.response.GenericResponse;
import ru.secretsanta.dto.response.UserShortResponse;
import ru.secretsanta.entity.User;
import ru.secretsanta.service.UserService;
import ru.secretsanta.mapper.UserMapper;
import ru.secretsanta.repository.UserRepository;



@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<UserShortResponse> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new RuntimeException("User is not authenticated");
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        User user = userService.getUserByName(username);

        return ResponseEntity.ok(UserMapper.toUserShortResponse(user));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-user")
    public ResponseEntity<GenericResponse> deleteUser(@RequestParam String username){
        userService.deleteUserByName(username);
        return ResponseEntity.ok(new GenericResponse("user was successfully deleted"));
    }
}
