package ru.secretsanta.controller.group;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.secretsanta.dto.request.CreateInviteRequest;
import ru.secretsanta.dto.response.GenericResponse;
import ru.secretsanta.dto.response.GroupInviteResponse;

import ru.secretsanta.entity.user.User;
import ru.secretsanta.service.group.GroupInviteService;
import ru.secretsanta.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupInviteController {

    private final GroupInviteService inviteService;
    private final UserService userService;

    @PostMapping("/invite")
    public ResponseEntity<GroupInviteResponse> createInvite(@RequestBody CreateInviteRequest req) {
        User inviter = userService.getCurrentUser();
        GroupInviteResponse resp = inviteService.createInvite(req, inviter);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/invites")
    public ResponseEntity<List<GroupInviteResponse>> getMyInvites() {
        User me = userService.getCurrentUser();
        return ResponseEntity.ok(inviteService.getInvitesForUser(me));
    }

    @PostMapping("/invites/{id}/accept")
    public ResponseEntity<GenericResponse> acceptInvite(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        inviteService.acceptInvite(id, currentUser);
        return ResponseEntity.ok(new GenericResponse("invite accepted"));
    }

    @PostMapping("/invites/{id}/decline")
    public ResponseEntity<GenericResponse> declineInvite(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        inviteService.declineInvite(id, currentUser);
        return ResponseEntity.ok(new GenericResponse("invite declined"));
    }

    @DeleteMapping("/invites/{id}")
    public ResponseEntity<GenericResponse> cancelInvite(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        inviteService.cancelInvite(id, currentUser);
        return ResponseEntity.ok(new GenericResponse("invite canceled"));
    }
}
