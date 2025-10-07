package ru.secretsanta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.secretsanta.entity.user.InviteToken;

import java.util.Optional;

public interface InviteTokenRepository extends JpaRepository<InviteToken, Long> {
    Optional<InviteToken> findByTokenAndUsedFalse(String token);
}
