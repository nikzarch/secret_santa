package ru.secretsanta.service;

import ru.secretsanta.entity.InviteToken;

import java.util.Optional;

public interface InviteService {
    InviteToken createInvite(String username);

    Optional<InviteToken> validateToken(String token);

    void markUsed(InviteToken invite);
}
