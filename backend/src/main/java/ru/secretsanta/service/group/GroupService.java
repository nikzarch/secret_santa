package ru.secretsanta.service.group;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.secretsanta.dto.request.*;
import ru.secretsanta.dto.response.GroupResponse;

import ru.secretsanta.dto.response.UserShortResponse;
import ru.secretsanta.entity.user.User;

import java.util.List;


public interface GroupService {
    void createGroup(CreateGroupRequest request, User owner);
    void addUserToGroup(AddUserToGroupRequest request, User owner);
    void kickUserFromGroup(KickUserFromGroupRequest request, User owner);
    void makeOtherUserOwner(OwnerTransferRequest request, User owner);
    void deleteGroup(String name, User owner);
    void deleteGroup(Long id, User owner);
    GroupResponse getInfoAboutGroup(GroupRequest request);

    Page<GroupResponse> getAllGroups(Pageable pageable);
    Page<GroupResponse> getGroupsByUser(Pageable pageable,User user);
    List<UserShortResponse> getGroupParticipants(GroupRequest request);
}
