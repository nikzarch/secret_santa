package ru.secretsanta.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.secretsanta.dto.request.AddEventRequest;
import ru.secretsanta.dto.request.AddParticipantToEventRequest;
import ru.secretsanta.dto.request.DisactiveEventRequest;
import ru.secretsanta.dto.response.AssignmentResponse;
import ru.secretsanta.entity.Event;
import ru.secretsanta.entity.Role;
import ru.secretsanta.entity.User;
import ru.secretsanta.repository.UserRepository;
import ru.secretsanta.service.AuthService;
import ru.secretsanta.service.EventService;
import ru.secretsanta.util.JWTUtil;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final AuthService authService;


    @PostMapping
    public ResponseEntity<String> addEvent(@RequestHeader("Authorization") String header,
                                           @RequestBody @Valid AddEventRequest request) {
        authService.isAdminByToken(header.substring(7));
        eventService.addEvent(request);
        return ResponseEntity.ok("Event added successfully");
    }

    @PostMapping("/participants")
    public ResponseEntity<String> addParticipant(@RequestHeader("Authorization") String header,
                                                 @RequestBody @Valid AddParticipantToEventRequest request) {
        authService.isAdminByToken(header.substring(7));
        eventService.addParticipantToEvent(request);
        return ResponseEntity.ok("Participant added successfully");
    }

    @PostMapping("/disactive")
    public ResponseEntity<String> disactiveEvent(@RequestHeader("Authorization") String header,
                                                 @RequestBody @Valid DisactiveEventRequest request) {
        authService.isAdminByToken(header.substring(7));
        eventService.disactiveEvent(request);
        return ResponseEntity.ok("Event disactivated successfully");
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents(@RequestHeader("Authorization") String header) {
        authService.isAdminByToken(header.substring(7));
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Event>> getEventsByUser(@PathVariable String username) {
        return ResponseEntity.ok(eventService.getEventsByUserName(username));
    }
    @PostMapping("/{eventId}/generate-assignments")
    public ResponseEntity<String> generateAssignments(@RequestHeader("Authorization") String header,
                                                      @PathVariable Long eventId) {
        authService.isAdminByToken(header.substring(7));
        eventService.generateAssignments(eventId);
        return ResponseEntity.ok("Assignments generated");
    }

    @GetMapping("/{eventId}/my-receiver")
    public ResponseEntity<AssignmentResponse> getMyReceiver(@RequestHeader("Authorization") String header,
                                              @PathVariable Long eventId) {
        String username = authService.getNameByToken(header.substring(7));
        return ResponseEntity.ok(new AssignmentResponse(eventService.getReceiverForUser(eventId, username).getName()));
    }

}
