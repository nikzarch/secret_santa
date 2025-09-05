package ru.secretsanta.dto.response;

import java.util.List;

public record GeneratedAssignmentsResponse(
        Long eventId,
        List<AssignmentDto> assignments
) {
    public record AssignmentDto(
            String santaName,
            String receiverName
    ) {
    }
}