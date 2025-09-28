package ru.secretsanta.service;

import ru.secretsanta.dto.request.AddEventRequest;
import ru.secretsanta.dto.request.AddParticipantToEventRequest;
import ru.secretsanta.dto.request.DisactiveEventRequest;
import ru.secretsanta.dto.response.EventResponse;
import ru.secretsanta.dto.response.EventWithParticipantsResponse;
import ru.secretsanta.dto.response.UserShortResponse;
import ru.secretsanta.entity.Event;
import ru.secretsanta.entity.User;
import ru.secretsanta.repository.EventRepository;

import java.util.List;

public interface EventService {

    void addEvent(AddEventRequest request);

    void addParticipantToEvent(AddParticipantToEventRequest request);

    void removeParticipantFromEvent(AddParticipantToEventRequest request);

    void disactiveEvent(DisactiveEventRequest request);

    List<EventResponse> getAllEvents();

    List<EventWithParticipantsResponse> getEventsByUserName(String name);

    List<UserShortResponse> getParticipants(Long eventId);

    void generateAssignments(Long eventId);

    UserShortResponse getReceiverForUser(Long eventId, String username);

}
