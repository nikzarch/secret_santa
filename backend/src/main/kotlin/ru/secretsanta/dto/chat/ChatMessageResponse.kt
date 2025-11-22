package ru.secretsanta.dto.chat

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
enum class ChatMessageRole {
    YOU,
    OTHER
}

data class ChatMessageResponse(
    val from: ChatMessageRole,
    val message: String,
    @JsonProperty("sent_at")
    val sentAt: Instant
)