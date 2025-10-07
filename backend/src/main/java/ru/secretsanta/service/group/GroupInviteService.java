package ru.secretsanta.service.group;

import ru.secretsanta.dto.request.CreateInviteRequest;
import ru.secretsanta.dto.response.GroupInviteResponse;
import ru.secretsanta.entity.user.User;

import java.util.List;

public interface GroupInviteService {
    GroupInviteResponse createInvite(CreateInviteRequest req, User inviter);
    List<GroupInviteResponse> getInvitesForUser(User user);
    void acceptInvite(Long inviteId, User invitee);
    void declineInvite(Long inviteId, User invitee);
    void cancelInvite(Long inviteId, User owner);
}
