package ru.secretsanta.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.secretsanta.dto.request.AddEventRequest;
import ru.secretsanta.dto.request.AddParticipantToEventRequest;
import ru.secretsanta.dto.request.DisactiveEventRequest;
import ru.secretsanta.dto.response.AssignmentResponse;
import ru.secretsanta.dto.response.EventResponse;
import ru.secretsanta.dto.response.EventWithParticipantsResponse;
import ru.secretsanta.entity.Event;
import ru.secretsanta.mapper.EventMapper;
import ru.secretsanta.service.EventService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Map<String, String>> addEvent(@RequestBody @Valid AddEventRequest request) {
        eventService.addEvent(request);
        return ResponseEntity.ok(Map.of("message", "Event added succesfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/participants")
    public ResponseEntity<Map<String, String>> addParticipant(@RequestBody @Valid AddParticipantToEventRequest request) {
        eventService.addParticipantToEvent(request);
        return ResponseEntity.ok(Map.of("message", "Participant added succesfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/disactive")
    public ResponseEntity<Map<String, String>> disactiveEvent(@RequestBody @Valid DisactiveEventRequest request) {
        eventService.disactiveEvent(request);
        return ResponseEntity.ok(Map.of("message", "Event disactivated succesfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents().stream().map(EventMapper::toEventResponse).toList());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<EventWithParticipantsResponse>> getEventsByUser(@PathVariable String username) {
        return ResponseEntity.ok(eventService.getEventsByUserName(username).stream().map(EventMapper::toEventWithParticipantsResponse).toList());
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEventUserParticipateIn(@PathVariable Long eventId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new RuntimeException("User is not authenticated");
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        Event event = eventService.getEventsByUserName(username)
                .stream().filter(ev -> ev.getId() == eventId)
                .findFirst()
                .orElseThrow();
        return ResponseEntity.ok(EventMapper.toEventResponse(event));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{eventId}/generate-assignments")
    public ResponseEntity<Map<String, String>> generateAssignments(@PathVariable Long eventId) {
        eventService.generateAssignments(eventId);
        return ResponseEntity.ok(Map.of("message", "Assignment generated successfully"));
    }

    @GetMapping("/{eventId}/my-receiver")
    public ResponseEntity<AssignmentResponse> getMyReceiver(Authentication authentication,
                                                            @PathVariable Long eventId) {
        String username = authentication.getName();
        return ResponseEntity.ok(
                new AssignmentResponse(eventService.getReceiverForUser(eventId, username).getName())
        );
    }
}
