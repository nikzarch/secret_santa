package ru.secretsanta.controller.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.secretsanta.dto.request.CreateEventRequest;
import ru.secretsanta.dto.request.DisactiveEventRequest;
import ru.secretsanta.dto.response.AssignmentResponse;
import ru.secretsanta.dto.response.EventResponse;
import ru.secretsanta.dto.response.EventWithParticipantsResponse;
import ru.secretsanta.dto.response.GenericResponse;
import ru.secretsanta.entity.user.User;
import ru.secretsanta.service.event.EventService;
import ru.secretsanta.service.user.UserService;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<GenericResponse> addEvent(@RequestBody @Valid CreateEventRequest request) {
        User currentUser = userService.getCurrentUser();
        eventService.addEvent(request,currentUser);
        return ResponseEntity.ok(new GenericResponse("Event added successfully"));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/disactive")
    public ResponseEntity<GenericResponse> disactiveEvent(@RequestBody @Valid DisactiveEventRequest request) {
        eventService.disactiveEvent(request);
        return ResponseEntity.ok(new GenericResponse("Event disactivated successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Page<EventResponse>> getAllEvents(Pageable pageable) {
        return ResponseEntity.ok(eventService.getAllEvents(pageable));
    }

    @GetMapping()
    public ResponseEntity<Page<EventWithParticipantsResponse>> getEventsForUser( @PageableDefault(page=0,size = 25,sort = "eventDate",direction =  Sort.Direction.DESC) Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(eventService.getEventsByUser(currentUser,pageable));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventWithParticipantsResponse> getEventUserParticipateIn(@PathVariable Long eventId) {
        User currentUser = userService.getCurrentUser();

        return ResponseEntity.ok(eventService.getEventUserParticipateIn(currentUser,eventId));
    }

    @PostMapping("/{eventId}/generate-assignments")
    public ResponseEntity<GenericResponse> generateAssignments(@PathVariable Long eventId) {
        eventService.generateAssignments(eventId, );
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
