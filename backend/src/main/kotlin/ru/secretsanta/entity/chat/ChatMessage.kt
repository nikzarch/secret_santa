package ru.secretsanta.entity.chat

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import ru.secretsanta.entity.event.Event
import ru.secretsanta.entity.user.User
import java.time.Instant

@Entity
@Table(name = "chat_messages")
class ChatMessage(
    @Id
    @GeneratedValue
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id", nullable = false)
    val sender: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id", nullable = false)
    val receiver: User,

    @Size(max = 200)
    @Column(nullable = false)
    val message: String,

    @ManyToOne
    val event: Event,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now()
)
