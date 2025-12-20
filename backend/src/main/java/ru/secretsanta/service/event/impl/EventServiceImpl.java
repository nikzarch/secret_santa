package ru.secretsanta.service.event.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.secretsanta.dto.request.CreateEventRequest;
import ru.secretsanta.dto.request.DisactiveEventRequest;
import ru.secretsanta.dto.response.EventResponse;
import ru.secretsanta.dto.response.EventWithParticipantsResponse;
import ru.secretsanta.dto.response.UserShortResponse;
import ru.secretsanta.entity.event.Event;
import ru.secretsanta.entity.event.SantaAssignment;
import ru.secretsanta.entity.group.Group;
import ru.secretsanta.entity.user.Role;
import ru.secretsanta.entity.user.User;
import ru.secretsanta.exception.common.NotFoundException;
import ru.secretsanta.exception.user.InsufficientPrivilegesException;
import ru.secretsanta.mapper.event.EventMapper;
import ru.secretsanta.mapper.common.PageMapper;
import ru.secretsanta.mapper.user.UserMapper;
import ru.secretsanta.repository.EventRepository;
import ru.secretsanta.repository.GroupRepository;
import ru.secretsanta.repository.UserRepository;
import ru.secretsanta.service.event.EventService;
import ru.secretsanta.util.AppConstants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Override
    @Transactional
    public void addEvent(CreateEventRequest request, User creator) {
        if (creator.getGroupsOwned().size() > AppConstants.MAX_EVENTS_PER_USER){
            throw new InsufficientPrivilegesException("you can't create that many events");
        }
        Group group = groupRepository.findById(request.groupId()).orElseThrow(() -> new NotFoundException("group not found"));
        if (group.getOwner().getId() != creator.getId() && creator.getRole() != Role.ADMIN){
            throw new InsufficientPrivilegesException("This group isnt yours and you cant make an event for it");
        }
        Event event = Event.builder()
                .name(request.name())
                .description(request.description())
                .eventDate(request.date() != null ? request.date() : LocalDate.now())
                .isActive(true)
                .assignmentsGenerated(false)
                .participants(group.getUsers().stream().toList())
                .build();
        eventRepository.save(event);
    }


    @Override
    @Transactional
    public void disactiveEvent(DisactiveEventRequest request) {
        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setActive(false);
        eventRepository.save(event);
    }

    @Override
    public Page<EventResponse> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable).map(EventMapper::toEventResponse);
    }
    @Override
    public Page<EventResponse> getAllActiveEvents(Pageable pageable){
        return eventRepository.findByIsActiveTrue(pageable).map(EventMapper::toEventResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventWithParticipantsResponse> getEventsByUser(User user, Pageable pageable) {
        return PageMapper.listToPage(eventRepository.findByParticipantsContaining(user,Pageable.unpaged()).stream().filter(event -> event.isActive()).map(EventMapper::toEventWithParticipantsResponse).toList(),pageable);
    }

    @Override
    public Page<UserShortResponse> getParticipants(Long eventId, Pageable pageable) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return PageMapper.listToPage(event.getParticipants().stream().map(UserMapper::toUserShortResponse).toList(),pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public EventWithParticipantsResponse getEventUserParticipateIn(User user, Long id){
        Event event = eventRepository.findById(id)
                .filter(ev -> ev.getParticipants().contains(user) )
                .orElseThrow(() -> new NotFoundException("event not found"));
        return EventMapper.toEventWithParticipantsResponse(event);
    }

    @Override
    @Transactional
    public void generateAssignments(Long eventId, User user) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        if (event.isAssignmentsGenerated()){
            throw new RuntimeException("Already generated");
        }
        if (!event.getParticipants().contains(user)){
            throw new InsufficientPrivilegesException("not your event receiver generate assignments");
        }
        List<User> participants = new ArrayList<>(event.getParticipants().stream().toList());
        if (participants.size() < 2) {
            throw new RuntimeException("Not enough participants");
        }

        Collections.shuffle(participants);

        List<User> receivers = new ArrayList<>(participants);
        Collections.shuffle(receivers);

        while (hasSelfAssignment(participants, receivers)) {
            Collections.shuffle(receivers);
        }

        for (int i = 0; i < participants.size(); i++) {
            User santa = participants.get(i);
            User receiver = receivers.get(i);

            SantaAssignment assignment = SantaAssignment.builder()
                    .event(event)
                    .santa(santa)
                    .receiver(receiver)
                    .build();

            event.getAssignments().add(assignment);
        }

        event.setAssignmentsGenerated(true);
        eventRepository.save(event);
    }

    private boolean hasSelfAssignment(List<User> santas, List<User> receivers) {
        for (int i = 0; i < santas.size(); i++) {
            if (santas.get(i).equals(receivers.get(i))) {
                return true; // кто-то выпал сам себе
            }
        }
        return false;
    }


    @Override
    public UserShortResponse getReceiverForUser(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return event.getAssignments().stream()
                .filter(a -> a.getSanta().equals(user))
                .map(SantaAssignment::getReceiver)
                .map(UserMapper::toUserShortResponse)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No assignment found"));
    }

    @Override
    public UserShortResponse getSantaForUser(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return event.getAssignments().stream()
                .filter(a -> a.getReceiver().equals(user))
                .map(SantaAssignment::getSanta)
                .map(UserMapper::toUserShortResponse)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No assignment found"));
    }

}
