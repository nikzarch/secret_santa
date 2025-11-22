package ru.secretsanta.mapper.chat

import ru.secretsanta.dto.chat.ChatMessageResponse
import ru.secretsanta.dto.chat.ChatMessageRole
import ru.secretsanta.entity.chat.ChatMessage

object ChatMessageMapper {
    fun toChatMessageResponse(chatMessage: ChatMessage, currentUserId: Long) =
        ChatMessageResponse(
            from = if (chatMessage.sender.id == currentUserId) ChatMessageRole.YOU else ChatMessageRole.OTHER,
            message = chatMessage.message,
            sentAt = chatMessage.createdAt
        )
}
