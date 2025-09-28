package ru.secretsanta.mapper;

import ru.secretsanta.dto.response.InviteResponse;
import ru.secretsanta.entity.InviteToken;

public class InviteMapper {

    public static InviteResponse toInviteResponse(InviteToken inviteToken) {
        return new InviteResponse(inviteToken.getUsername(), inviteToken.getToken());
    }
}
