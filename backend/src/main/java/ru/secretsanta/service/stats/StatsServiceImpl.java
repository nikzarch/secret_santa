package ru.secretsanta.service.stats;

import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.secretsanta.dto.response.GroupResponse;
import ru.secretsanta.dto.response.StatsResponse;
import ru.secretsanta.service.event.EventService;
import ru.secretsanta.service.group.GroupService;
import ru.secretsanta.service.user.UserService;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService{
    private final UserService userService;
    private final EventService eventService;
    private final GroupService groupService;
    @Override
    public StatsResponse getAllStats() {
        Integer usersRegistered = userService.getUsersRegistered();
        Integer activeEvents = eventService.getAllActiveEvents(Pageable.unpaged()).getContent().size();
        Integer events = eventService.getAllEvents(Pageable.unpaged()).getContent().size();
        Integer groups = groupService.getAllGroups(Pageable.unpaged()).getContent().size();
        return new StatsResponse(usersRegistered,activeEvents,events,groups);
    }
}
