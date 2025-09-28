package ru.secretsanta.service;

import ru.secretsanta.dto.response.InviteResponse;
import ru.secretsanta.entity.InviteToken;

import java.util.Optional;

public interface InviteService {
    InviteResponse createInvite(String username);

    Optional<InviteToken> validateToken(String token);

    void markUsed(InviteToken invite);
}
