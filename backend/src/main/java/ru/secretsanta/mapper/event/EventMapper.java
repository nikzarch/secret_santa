package ru.secretsanta.mapper.event;

import ru.secretsanta.dto.response.EventResponse;
import ru.secretsanta.dto.response.EventWithParticipantsResponse;
import ru.secretsanta.entity.event.Event;
import ru.secretsanta.mapper.user.UserMapper;

import java.util.stream.Collectors;

public class EventMapper {

    public static EventResponse toEventResponse(Event event) {
        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getEventDate(),
                event.isActive(),
                event.isAssignmentsGenerated()
        );
    }

    public static EventWithParticipantsResponse toEventWithParticipantsResponse(Event event) {
        return new EventWithParticipantsResponse(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getGroup().getOwner().getName(),
                event.getEventDate(),
                event.isActive(),
                event.isAssignmentsGenerated(),
                event.getGroup().getUsers().stream()
                        .map(UserMapper::toUserShortResponse)
                        .collect(Collectors.toList())
        );
    }
}