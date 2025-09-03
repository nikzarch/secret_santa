package ru.secretsanta.dto.response;

import ru.secretsanta.dto.response.UserShortResponse;

import java.time.LocalDate;
import java.util.List;

public record EventWithParticipantsResponse(
        Long id,
        String name,
        String description,
        LocalDate eventDate,
        boolean isActive,
        boolean assignmentsGenerated,
        List<UserShortResponse> participants
) {}
