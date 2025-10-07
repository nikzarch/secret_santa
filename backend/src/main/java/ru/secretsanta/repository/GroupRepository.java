package ru.secretsanta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.secretsanta.entity.group.Group;
import ru.secretsanta.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Page<Group> findAllByOwner(User owner, Pageable pageable);
    List<Group> findAllByOwner(User owner);
    @Query("SELECT g FROM Group g WHERE g.owner = :user OR :user MEMBER OF g.users")
    List<Group> findAllByOwnerOrParticipant(@Param("user") User user);
    Optional<Group> findByOwnerAndName(User owner, String name);
    Page<Group> findByName(String name, Pageable pageable);
    Optional<Group> findById(Long id);
}
