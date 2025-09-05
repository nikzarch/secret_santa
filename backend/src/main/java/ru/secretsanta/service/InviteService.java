package ru.secretsanta.service;

import ru.secretsanta.entity.InviteToken;

import java.util.Optional;

public interface InviteService {
    public InviteToken createInvite(String username);
    public Optional<InviteToken> validateToken(String token);
    public void markUsed(InviteToken invite);
}
