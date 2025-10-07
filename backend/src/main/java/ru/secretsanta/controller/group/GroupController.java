package ru.secretsanta.controller.group;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.secretsanta.dto.request.*;
import ru.secretsanta.dto.response.GenericResponse;
import ru.secretsanta.dto.response.GroupResponse;
import ru.secretsanta.dto.response.UserShortResponse;
import ru.secretsanta.entity.user.User;
import ru.secretsanta.service.group.GroupService;
import ru.secretsanta.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<GenericResponse> createGroup(@RequestBody CreateGroupRequest request) {
        User currentUser = userService.getCurrentUser();
        groupService.createGroup(request, currentUser);
        return ResponseEntity.ok(new GenericResponse("group successfully created"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-user")
    public ResponseEntity<GenericResponse> addUserToGroup(@RequestBody AddUserToGroupRequest request) {
        User currentUser = userService.getCurrentUser();
        groupService.addUserToGroup(request, currentUser);
        return ResponseEntity.ok(new GenericResponse("user successfully added"));
    }

    @PostMapping("/kick-user")
    public ResponseEntity<GenericResponse> kickUserFromGroup(@RequestBody KickUserFromGroupRequest request) {
        User currentUser = userService.getCurrentUser();
        groupService.kickUserFromGroup(request, currentUser);
        return ResponseEntity.ok(new GenericResponse("user kicked"));
    }

    @PostMapping("/transfer-owner")
    public ResponseEntity<GenericResponse> makeOtherUserOwner(@RequestBody OwnerTransferRequest request) {
        User currentUser = userService.getCurrentUser();
        groupService.makeOtherUserOwner(request, currentUser);
        return ResponseEntity.ok(new GenericResponse(request.username() + " now owner"));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<GenericResponse> deleteGroup(@PathVariable String name) {
        User currentUser = userService.getCurrentUser();
        groupService.deleteGroup(name, currentUser);
        return ResponseEntity.ok(new GenericResponse("group was deleted"));
    }

    @DeleteMapping("/by-id/{id}")
    public ResponseEntity<GenericResponse> deleteGroupById(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        groupService.deleteGroup(id, currentUser);
        return ResponseEntity.ok(new GenericResponse("group was deleted"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> getGroupInfo(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getInfoAboutGroup(new GroupRequest(id)));
    }


    @GetMapping
    public ResponseEntity<Page<GroupResponse>> getGroupsForUser(Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(groupService.getGroupsByUser(pageable, currentUser));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Page<GroupResponse>> getAllGroups(Pageable pageable){
        return ResponseEntity.ok(groupService.getAllGroups(pageable));
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<UserShortResponse>> getGroupParticipants(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getGroupParticipants(new GroupRequest(id)));
    }



}

