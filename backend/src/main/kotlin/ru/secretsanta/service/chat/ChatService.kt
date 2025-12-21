package ru.secretsanta.service.chat

import org.springframework.data.domain.Pageable
import ru.secretsanta.dto.chat.ChatMessageResponse

interface ChatService {
    fun getMessagesWithReceiver(currentUserId: Long, eventId: Long, pageable: Pageable): List<ChatMessageResponse>
    fun getMessagesWithSanta(currentUserId: Long, eventId: Long, pageable: Pageable): List<ChatMessageResponse>
    fun sendMessageFromUserToUser(
        userFromId: Long, userToId: Long, eventId: Long, message: String, fromSanta: Boolean
    )
}