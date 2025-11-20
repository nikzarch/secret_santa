package ru.secretsanta.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AssignmentResponse(
        @JsonProperty("receiver_id")
        Long receiverId,
        String receiver) {
}
