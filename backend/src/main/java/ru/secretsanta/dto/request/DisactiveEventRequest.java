package ru.secretsanta.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record DisactiveEventRequest(
        @JsonProperty("event_id") @NotNull Long eventId
) {
}
