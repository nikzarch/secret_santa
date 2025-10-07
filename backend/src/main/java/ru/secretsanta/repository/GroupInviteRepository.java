package ru.secretsanta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.secretsanta.entity.group.Group;
import ru.secretsanta.entity.group.GroupInvite;
import ru.secretsanta.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface GroupInviteRepository extends JpaRepository<GroupInvite, Long> {
    List<GroupInvite> findAllByInvitee(User invitee);
    List<GroupInvite> findAllByInviter(User inviter);
    Optional<GroupInvite> findByGroupAndInvitee(Group group, User invitee);
    Optional<GroupInvite> findByIdAndInvitee(Long id, User invitee);
    Optional<GroupInvite> findByIdAndGroup(Long id, Group group);
}
