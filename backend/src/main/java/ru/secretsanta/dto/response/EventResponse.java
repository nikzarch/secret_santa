package ru.secretsanta.dto.response;

import java.time.LocalDate;

public record EventResponse(
        Long id,
        String name,
        String description,
        LocalDate eventDate,
        boolean isActive,
        boolean assignmentsGenerated
) {}
