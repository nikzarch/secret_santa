package ru.secretsanta.service;

import ru.secretsanta.dto.request.AddEventRequest;
import ru.secretsanta.dto.request.AddParticipantToEventRequest;
import ru.secretsanta.dto.request.DisactiveEventRequest;
import ru.secretsanta.entity.Event;
import ru.secretsanta.entity.User;

import java.util.List;

public interface EventService {

    void addEvent(AddEventRequest request);

    void addParticipantToEvent(AddParticipantToEventRequest request);

    void removeParticipantFromEvent(AddParticipantToEventRequest request);

    void disactiveEvent(DisactiveEventRequest request);

    List<Event> getAllEvents();

    List<Event> getEventsByUserName(String name);

    List<User> getParticipants(Long eventId);

    void generateAssignments(Long eventId);

    User getReceiverForUser(Long eventId, String username);

}
