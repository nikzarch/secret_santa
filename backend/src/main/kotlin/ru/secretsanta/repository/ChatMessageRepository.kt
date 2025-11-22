package ru.secretsanta.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.secretsanta.entity.chat.ChatMessage
import ru.secretsanta.entity.event.Event
import ru.secretsanta.entity.user.User
import java.time.Instant


@Repository
interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {
    fun findByEventAndReceiver(event: Event, receiver: User, pageable: Pageable) : List<ChatMessage>
    fun findByCreatedAtLessThan(time: Instant)
    @Query("""
        SELECT m FROM ChatMessage m
        WHERE m.event = :event
          AND ((m.sender = :user1 AND m.receiver = :user2)
            OR (m.sender = :user2 AND m.receiver = :user1))
        ORDER BY m.createdAt DESC
    """)
    fun findByEventAndUsers(
        @Param("event") event: Event,
        @Param("user1") user1: User,
        @Param("user2") user2: User,
        pageable: Pageable
    ): List<ChatMessage>
}