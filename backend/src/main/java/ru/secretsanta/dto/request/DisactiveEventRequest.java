package ru.secretsanta.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record DisactiveEventRequest(
        @NotBlank Long eventId
        ) {}
