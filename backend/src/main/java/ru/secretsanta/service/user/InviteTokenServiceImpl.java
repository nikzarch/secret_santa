package ru.secretsanta.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.secretsanta.dto.response.InviteResponse;
import ru.secretsanta.entity.user.InviteToken;
import ru.secretsanta.mapper.user.InviteMapper;
import ru.secretsanta.repository.InviteTokenRepository;
import ru.secretsanta.util.JWTUtil;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InviteTokenServiceImpl implements InviteService {
    private final JWTUtil jwtUtil;
    private final InviteTokenRepository inviteTokenRepository;

    public InviteResponse createInvite(String username) {
        InviteToken invite = new InviteToken();
        String token = jwtUtil.generateToken(username, "USER");
        invite.setToken(token);
        invite.setUsername(username);
        invite.setExpiresAt(LocalDateTime.now().plusDays(7));
        return InviteMapper.toInviteResponse(inviteTokenRepository.save(invite));
    }

    public Optional<InviteToken> validateToken(String token) {
        return inviteTokenRepository.findByTokenAndUsedFalse(token)
                .filter(inv -> inv.getExpiresAt().isAfter(LocalDateTime.now()));
    }

    public void markUsed(InviteToken invite) {
        invite.setUsed(true);
        inviteTokenRepository.save(invite);
    }
}
