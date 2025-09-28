package ru.secretsanta.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<EventResponse> getAllEvents(Pageable pageable);

    Page<EventWithParticipantsResponse> getEventsByUserName(String name, Pageable pageable);
    EventWithParticipantsResponse getEventUserParticipateIn(String name, Long eventId);

    Page<UserShortResponse> getParticipants(Long eventId, Pageable pageable);

    void generateAssignments(Long eventId);

    UserShortResponse getReceiverForUser(Long eventId, String username);

}
