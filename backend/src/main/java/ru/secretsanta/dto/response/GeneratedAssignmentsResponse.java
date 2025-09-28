package ru.secretsanta.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GeneratedAssignmentsResponse(
        @JsonProperty("event_id")
        Long eventId,
        List<AssignmentDto> assignments
) {
    public record AssignmentDto(
            @JsonProperty("santa_name")
            String santaName,
            @JsonProperty("receiver_name")
            String receiverName
    ) {
    }
}