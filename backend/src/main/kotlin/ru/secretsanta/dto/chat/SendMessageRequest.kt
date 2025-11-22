package ru.secretsanta.dto.chat

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size

data class SendMessageRequest(
    @Size(max = 200)
    val message: String,
    @JsonProperty("event_id")
    val eventId: Long
)
