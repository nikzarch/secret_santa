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
import ru.secretsanta.service.UserService;


@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserShortResponse> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new RuntimeException("User is not authenticated");
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        UserShortResponse userShortResponse = userService.getUserByName(username);

        return ResponseEntity.ok(userShortResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-user")
    public ResponseEntity<GenericResponse> deleteUser(@RequestParam String username) {
        userService.deleteUserByName(username);
        return ResponseEntity.ok(new GenericResponse("user was successfully deleted"));
    }
}
