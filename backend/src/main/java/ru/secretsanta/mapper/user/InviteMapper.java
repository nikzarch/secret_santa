package ru.secretsanta.mapper.user;

import ru.secretsanta.dto.response.InviteResponse;
import ru.secretsanta.entity.user.InviteToken;

public class InviteMapper {

    public static InviteResponse toInviteResponse(InviteToken inviteToken) {
        return new InviteResponse(inviteToken.getUsername(), inviteToken.getToken());
    }
}
