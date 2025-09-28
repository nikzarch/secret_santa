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
import ru.secretsanta.dto.response.GenericResponse;
import ru.secretsanta.entity.Event;
import ru.secretsanta.mapper.EventMapper;
import ru.secretsanta.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<GenericResponse> addEvent(@RequestBody @Valid AddEventRequest request) {
        eventService.addEvent(request);
        return ResponseEntity.ok(new GenericResponse("Event added successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/participants")
    public ResponseEntity<GenericResponse> addParticipant(@RequestBody @Valid AddParticipantToEventRequest request) {
        eventService.addParticipantToEvent(request);
        return ResponseEntity.ok(new GenericResponse("Participant added successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/disactive")
    public ResponseEntity<GenericResponse> disactiveEvent(@RequestBody @Valid DisactiveEventRequest request) {
        eventService.disactiveEvent(request);
        return ResponseEntity.ok(new GenericResponse("Event disactivated successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<EventWithParticipantsResponse>> getEventsByUser(@PathVariable String username) {
        return ResponseEntity.ok(eventService.getEventsByUserName(username));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventWithParticipantsResponse> getEventUserParticipateIn(@PathVariable Long eventId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new RuntimeException("User is not authenticated");
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        EventWithParticipantsResponse event = eventService.getEventsByUserName(username)
                .stream().filter(ev -> ev.id() == eventId)
                .findFirst()
                .orElseThrow();
        return ResponseEntity.ok(event);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{eventId}/generate-assignments")
    public ResponseEntity<GenericResponse> generateAssignments(@PathVariable Long eventId) {
        eventService.generateAssignments(eventId);
        return ResponseEntity.ok(new GenericResponse("Assignment generated successfully"));
    }

    @GetMapping("/{eventId}/my-receiver")
    public ResponseEntity<AssignmentResponse> getMyReceiver(Authentication authentication,
                                                            @PathVariable Long eventId) {
        String username = authentication.getName();
        return ResponseEntity.ok(
                new AssignmentResponse(eventService.getReceiverForUser(eventId, username).name())
        );
    }
}
