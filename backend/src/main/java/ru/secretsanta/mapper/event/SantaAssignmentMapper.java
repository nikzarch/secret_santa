package ru.secretsanta.mapper.event;

import ru.secretsanta.dto.response.GeneratedAssignmentsResponse;
import ru.secretsanta.entity.event.SantaAssignment;

public class SantaAssignmentMapper {

    public static GeneratedAssignmentsResponse.AssignmentDto toAssignmentDto(SantaAssignment assignment) {
        return new GeneratedAssignmentsResponse.AssignmentDto(
                assignment.getSanta().getName(),
                assignment.getReceiver().getName()
        );
    }
}