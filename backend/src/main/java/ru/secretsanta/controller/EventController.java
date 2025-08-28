package ru.secretsanta.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.secretsanta.dto.request.AddEventRequest;
import ru.secretsanta.dto.request.AddParticipantToEventRequest;
import ru.secretsanta.dto.request.DisactiveEventRequest;
import ru.secretsanta.dto.response.AssignmentResponse;
import ru.secretsanta.entity.Event;
import ru.secretsanta.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> addEvent(@RequestBody @Valid AddEventRequest request) {
        eventService.addEvent(request);
        return ResponseEntity.ok("Event added successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/participants")
    public ResponseEntity<String> addParticipant(@RequestBody @Valid AddParticipantToEventRequest request) {
        eventService.addParticipantToEvent(request);
        return ResponseEntity.ok("Participant added successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/disactive")
    public ResponseEntity<String> disactiveEvent(@RequestBody @Valid DisactiveEventRequest request) {
        eventService.disactiveEvent(request);
        return ResponseEntity.ok("Event disactivated successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Event>> getEventsByUser(@PathVariable String username) {
        return ResponseEntity.ok(eventService.getEventsByUserName(username));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{eventId}/generate-assignments")
    public ResponseEntity<String> generateAssignments(@PathVariable Long eventId) {
        eventService.generateAssignments(eventId);
        return ResponseEntity.ok("Assignments generated");
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
