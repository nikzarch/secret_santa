package ru.secretsanta.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.secretsanta.dto.response.InviteResponse;
import ru.secretsanta.entity.InviteToken;
import ru.secretsanta.service.InviteService;

@RestController
@RequestMapping("/api/v1/invites")
@RequiredArgsConstructor
public class InviteController {
    private final InviteService inviteService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<InviteResponse> createInvite(@RequestParam String username) {
        InviteToken invite = inviteService.createInvite(username);
        return ResponseEntity.ok(new InviteResponse(invite.getUsername(), invite.getToken()));
    }
}
