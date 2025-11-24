package ru.secretsanta.entity.group;

import jakarta.persistence.*;
import lombok.*;
import ru.secretsanta.entity.user.User;

import java.time.Instant;

@Entity
@Table(name = "group_invites",
        uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "invitee_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GroupInvite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "inviter_id", nullable = false)
    private User inviter;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "invitee_id", nullable = false)
    private User invitee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InviteStatus status;

    @Column(nullable = false)
    private Instant createdAt;
}
