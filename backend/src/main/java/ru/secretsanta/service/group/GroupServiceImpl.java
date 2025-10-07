package ru.secretsanta.service.group;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.secretsanta.dto.request.*;
import ru.secretsanta.dto.response.GroupResponse;
import ru.secretsanta.dto.response.UserShortResponse;
import ru.secretsanta.entity.group.Group;
import ru.secretsanta.entity.user.User;
import ru.secretsanta.exception.user.InsufficientPrivilegesException;
import ru.secretsanta.exception.common.NotFoundException;
import ru.secretsanta.exception.common.TooMuchItemsException;
import ru.secretsanta.mapper.group.GroupMapper;
import ru.secretsanta.mapper.common.PageMapper;
import ru.secretsanta.mapper.user.UserMapper;
import ru.secretsanta.repository.GroupRepository;
import ru.secretsanta.repository.UserRepository;

import java.util.List;

import static ru.secretsanta.util.AppConstants.MAX_GROUPS_FOR_USER;
import static ru.secretsanta.util.AppConstants.MAX_USERS_PER_GROUP;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;



    @Override
    public void createGroup(CreateGroupRequest request, User owner) {
        if (groupRepository.findAllByOwner(owner, Pageable.unpaged()).stream().count() > MAX_GROUPS_FOR_USER) {
            throw new TooMuchItemsException("too much groups for " + owner.getName());
        }
        Group group = Group.builder().name(request.name()).owner(owner).build();
        group.getUsers().add(owner);
        groupRepository.save(group);
    }

    @Override
    public void addUserToGroup(AddUserToGroupRequest request, User owner) {
        //TODO: make it admin function
        Group group = groupRepository.findByOwnerAndName(owner,request.groupName()).orElseThrow(() -> new NotFoundException("group not found or it is not yours"));
        User userToAdd = userRepository.findByName(request.username()).orElseThrow(() -> new NotFoundException("user not found"));
        if (group.getUsers().size() > MAX_USERS_PER_GROUP){
            throw new TooMuchItemsException("too much users for group");
        }
        group.getUsers().add(userToAdd);
        groupRepository.save(group);
    }

    @Override
    public void kickUserFromGroup(KickUserFromGroupRequest request, User owner) {
        Group group = groupRepository.findByOwnerAndName(owner,request.groupName()).orElseThrow(() -> new NotFoundException("group not found or it is not yours"));
        User userToKick = userRepository.findByName(request.username()).orElseThrow(() -> new NotFoundException("user not found"));
        group.getUsers().remove(userToKick);
        groupRepository.save(group);
    }

    @Override
    public void makeOtherUserOwner(OwnerTransferRequest request, User owner) {
        Group group = groupRepository.findByOwnerAndName(owner,request.groupName()).orElseThrow(() -> new NotFoundException("group not found or it is not yours"));
        User userToBecomeOwner = userRepository.findByName(request.username()).orElseThrow(() -> new NotFoundException("user not found"));
        group.setOwner(owner);
        groupRepository.save(group);
    }

    @Override
    public void deleteGroup(String name, User owner) {
        Group group = groupRepository.findByOwnerAndName(owner,name).orElseThrow(() -> new NotFoundException("group not found or it is not yours"));
        groupRepository.delete(group);
    }

    @Override
    public void deleteGroup(Long id, User owner) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new NotFoundException("group not found "));
        if (group.getOwner().getId() == owner.getId()){
            groupRepository.delete(group);
        }else{
            throw new InsufficientPrivilegesException("it is not yours");
        }
    }

    @Override
    public GroupResponse getInfoAboutGroup(GroupRequest request) {
        return GroupMapper.toGroupResponse(groupRepository.findById(request.id()).orElseThrow(() -> new NotFoundException("group not found ")));
    }

    @Override
    public Page<GroupResponse> getAllGroups(Pageable pageable) {
        return PageMapper.listToPage(groupRepository.findAll().stream().map(GroupMapper::toGroupResponse).toList(),pageable);
    }
    @Override
    public Page<GroupResponse> getGroupsByUser(Pageable pageable,User user){

        return PageMapper.listToPage(groupRepository.findAllByOwnerOrParticipant(user).stream().map(GroupMapper::toGroupResponse).toList(),pageable);
    }

    @Override
    public List<UserShortResponse> getGroupParticipants(GroupRequest request) {
        return groupRepository.findById(request.id()).orElseThrow(() -> new NotFoundException("group not found ")).getUsers().stream().map(UserMapper::toUserShortResponse).toList();
    }
}
