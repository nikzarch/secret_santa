package ru.secretsanta.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.secretsanta.dto.response.GenericResponse;
import ru.secretsanta.dto.response.UserShortResponse;
import ru.secretsanta.mapper.user.UserMapper;
import ru.secretsanta.service.user.UserService;


@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserShortResponse> me() {


        UserShortResponse userShortResponse = UserMapper.toUserShortResponse(userService.getCurrentUser());

        return ResponseEntity.ok(userShortResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-user")
    public ResponseEntity<GenericResponse> deleteUser(@RequestParam String username) {
        userService.deleteUserByName(username);
        return ResponseEntity.ok(new GenericResponse("user was successfully deleted"));
    }
}
