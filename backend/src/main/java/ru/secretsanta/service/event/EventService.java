package ru.secretsanta.service.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.secretsanta.dto.request.CreateEventRequest;
import ru.secretsanta.dto.request.DisactiveEventRequest;
import ru.secretsanta.dto.response.EventResponse;
import ru.secretsanta.dto.response.EventWithParticipantsResponse;
import ru.secretsanta.dto.response.UserShortResponse;
import ru.secretsanta.entity.user.User;

public interface EventService {

    void addEvent(CreateEventRequest request, User creator);


    void disactiveEvent(DisactiveEventRequest request);

    Page<EventResponse> getAllEvents(Pageable pageable);
    Page<EventResponse> getAllActiveEvents(Pageable pageable);

    Page<EventWithParticipantsResponse> getEventsByUser(User user, Pageable pageable);
    EventWithParticipantsResponse getEventUserParticipateIn(User user, Long eventId);

    Page<UserShortResponse> getParticipants(Long eventId, Pageable pageable);

    void generateAssignments(Long eventId, User user);

    UserShortResponse getReceiverForUser(Long eventId, Long userId);
    UserShortResponse getSantaForUser(Long eventId, Long userId);

}
