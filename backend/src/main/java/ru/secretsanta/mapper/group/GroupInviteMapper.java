package ru.secretsanta.mapper.group;

import ru.secretsanta.dto.response.GroupInviteResponse;
import ru.secretsanta.entity.group.GroupInvite;

public class GroupInviteMapper {
    public static GroupInviteResponse toGroupInviteResponse(GroupInvite i) {
        return new GroupInviteResponse(
                i.getId(),
                i.getGroup().getId(),
                i.getGroup().getName(),
                i.getInviter().getId(),
                i.getInviter().getName(),
                i.getInvitee().getId(),
                i.getInvitee().getName(),
                i.getStatus().name(),
                i.getCreatedAt()
        );
    }
}
