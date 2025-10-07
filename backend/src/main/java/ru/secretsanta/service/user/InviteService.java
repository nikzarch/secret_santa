package ru.secretsanta.service.user;

import ru.secretsanta.dto.response.InviteResponse;
import ru.secretsanta.entity.user.InviteToken;

import java.util.Optional;

public interface InviteService {
    InviteResponse createInvite(String username);

    Optional<InviteToken> validateToken(String token);

    void markUsed(InviteToken invite);
}
