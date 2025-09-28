package ru.secretsanta.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record EventWithParticipantsResponse(
        Long id,
        String name,
        String description,
        @JsonProperty("event_date")
        LocalDate eventDate,
        @JsonProperty("is_active")
        boolean isActive,
        @JsonProperty("assignments_generated")
        boolean assignmentsGenerated,
        List<UserShortResponse> participants
) {
}
