package ru.secretsanta.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DisactiveEventRequest(
        @NotBlank Long eventId
) {
}
