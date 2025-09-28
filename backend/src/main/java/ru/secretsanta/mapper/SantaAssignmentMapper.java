package ru.secretsanta.mapper;

import ru.secretsanta.dto.response.GeneratedAssignmentsResponse;
import ru.secretsanta.entity.SantaAssignment;

public class SantaAssignmentMapper {

    public static GeneratedAssignmentsResponse.AssignmentDto toAssignmentDto(SantaAssignment assignment) {
        return new GeneratedAssignmentsResponse.AssignmentDto(
                assignment.getSanta().getName(),
                assignment.getReceiver().getName()
        );
    }
}