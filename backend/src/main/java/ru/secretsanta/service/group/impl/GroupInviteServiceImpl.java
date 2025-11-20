package ru.secretsanta.service.group.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.secretsanta.dto.request.CreateInviteRequest;
import ru.secretsanta.dto.response.GroupInviteResponse;

import ru.secretsanta.entity.group.Group;
import ru.secretsanta.entity.group.GroupInvite;
import ru.secretsanta.entity.group.InviteStatus;
import ru.secretsanta.entity.user.User;
import ru.secretsanta.exception.common.NotFoundException;
import ru.secretsanta.exception.common.TooMuchItemsException;
import ru.secretsanta.exception.user.InsufficientPrivilegesException;
import ru.secretsanta.exception.user.InviteStatusException;
import ru.secretsanta.mapper.group.GroupInviteMapper;
import ru.secretsanta.repository.GroupInviteRepository;
import ru.secretsanta.repository.GroupRepository;
import ru.secretsanta.repository.UserRepository;
import ru.secretsanta.service.group.GroupInviteService;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static ru.secretsanta.util.AppConstants.MAX_USERS_PER_GROUP;
@Service
@RequiredArgsConstructor
public class GroupInviteServiceImpl implements GroupInviteService {

    private final GroupInviteRepository inviteRepo;
    private final GroupRepository groupRepo;
    private final UserRepository userRepo;

    @Override
    public GroupInviteResponse createInvite(CreateInviteRequest req, User inviter) {
        Group group = groupRepo.findById(req.groupId()).orElseThrow(() -> new NotFoundException("Group not found"));
        if (!group.getOwner().getId().equals(inviter.getId())) {
            throw new InsufficientPrivilegesException("Only owner can invite");
        }
        User invitee = userRepo.findByName(req.username()).orElseThrow(() -> new NotFoundException("User not found"));

        inviteRepo.findByGroupAndInvitee(group, invitee).ifPresent(inv -> {
            if (inv.getStatus() == InviteStatus.PENDING) {
                throw new InviteStatusException("Invite already sent");
            }
        });

        GroupInvite invite = GroupInvite.builder()
                .group(group)
                .inviter(inviter)
                .invitee(invitee)
                .status(InviteStatus.PENDING)
                .createdAt(Instant.now())
                .build();
        invite = inviteRepo.save(invite);
        return GroupInviteMapper.toGroupInviteResponse(invite);
    }

    @Override
    public List<GroupInviteResponse> getInvitesForUser(User user) {
        return inviteRepo.findAllByInvitee(user).stream().filter(groupInvite -> groupInvite.getStatus() == InviteStatus.PENDING)
                .map(GroupInviteMapper::toGroupInviteResponse)
                .toList();
    }

    @Override
    public void acceptInvite(Long inviteId, User invitee) {
        GroupInvite inv = inviteRepo.findByIdAndInvitee(inviteId, invitee)
                .orElseThrow(() -> new NotFoundException("Invite not found"));
        if (inv.getStatus() != InviteStatus.PENDING) throw new InviteStatusException("Invite not pending");

        Group group = inv.getGroup();
        if (group.getUsers().size() >= MAX_USERS_PER_GROUP) throw new TooMuchItemsException("Group is full");

        group.getUsers().add(invitee);
        groupRepo.save(group);

        inv.setStatus(InviteStatus.ACCEPTED);
        inviteRepo.save(inv);
    }

    @Override
    public void declineInvite(Long inviteId, User invitee) {
        GroupInvite inv = inviteRepo.findByIdAndInvitee(inviteId, invitee)
                .orElseThrow(() -> new NotFoundException("Invite not found"));
        if (inv.getStatus() != InviteStatus.PENDING) throw new InviteStatusException("Invite not pending");
        inv.setStatus(InviteStatus.REJECTED);
        inviteRepo.save(inv);
    }

    @Override
    public void cancelInvite(Long inviteId, User owner) {
        GroupInvite inv = inviteRepo.findById(inviteId).orElseThrow(() -> new NotFoundException("Invite not found"));
        if (!inv.getGroup().getOwner().getId().equals(owner.getId())) {
            throw new InsufficientPrivilegesException("Only owner can cancel invite");
        }
        if (inv.getStatus() != InviteStatus.PENDING) throw new InviteStatusException("Cannot cancel non-pending invite");
        inv.setStatus(InviteStatus.CANCELLED);
        inviteRepo.save(inv);
    }
}
