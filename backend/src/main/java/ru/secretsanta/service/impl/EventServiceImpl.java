package ru.secretsanta.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.secretsanta.dto.request.AddEventRequest;
import ru.secretsanta.dto.request.AddParticipantToEventRequest;
import ru.secretsanta.dto.request.DisactiveEventRequest;
import ru.secretsanta.entity.Event;
import ru.secretsanta.entity.User;
import ru.secretsanta.entity.SantaAssignment;
import ru.secretsanta.exception.UserNotFoundException;
import ru.secretsanta.repository.EventRepository;
import ru.secretsanta.repository.UserRepository;
import ru.secretsanta.service.EventService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public void addEvent(AddEventRequest request) {
        Event event = Event.builder()
                .name(request.name())
                .description(request.description())
                .eventDate(request.date() != null ? request.date() : LocalDate.now())
                .isActive(true)
                .assignmentsGenerated(false)
                .build();
        eventRepository.save(event);
    }

    @Override
    public void addParticipantToEvent(AddParticipantToEventRequest request) {
        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));
        User user = userRepository.findByName(request.username())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!event.getParticipants().contains(user)) {
            event.getParticipants().add(user);
            eventRepository.save(event);
        }
    }

    @Override
    public void removeParticipantFromEvent(AddParticipantToEventRequest request) {
        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));
        User user = userRepository.findByName(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (event.getParticipants().contains(user)) {
            event.getParticipants().remove(user);
            eventRepository.save(event);
        }
    }

    @Override
    public void disactiveEvent(DisactiveEventRequest request) {
        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setActive(false);
        eventRepository.save(event);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> getEventsByUserName(String name) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return eventRepository.findByParticipantsContaining(user);
    }

    @Override
    public List<User> getParticipants(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return event.getParticipants();
    }
    @Override
    public void generateAssignments(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        List<User> participants = new ArrayList<>(event.getParticipants());
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
    public User getReceiverForUser(Long eventId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        User user = userRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return event.getAssignments().stream()
                .filter(a -> a.getSanta().equals(user))
                .map(SantaAssignment::getReceiver)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No assignment found"));
    }

}
