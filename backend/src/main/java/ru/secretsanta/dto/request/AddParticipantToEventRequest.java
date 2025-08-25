package ru.secretsanta.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddParticipantToEventRequest(
        @NotNull @JsonProperty("event_id") Long eventId,
        @NotBlank String username
) {
}
