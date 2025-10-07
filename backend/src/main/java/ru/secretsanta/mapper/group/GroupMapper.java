package ru.secretsanta.mapper.group;

import ru.secretsanta.dto.response.GroupResponse;
import ru.secretsanta.entity.group.Group;
import ru.secretsanta.mapper.user.UserMapper;

public class GroupMapper {
    public static GroupResponse toGroupResponse(Group group){
        return new GroupResponse(group.getId(), group.getName(),UserMapper.toUserShortResponse(group.getOwner()), group.getUsers().stream().map(UserMapper::toUserShortResponse).toList());
    }
}
