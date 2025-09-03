package ru.secretsanta.mapper;

import ru.secretsanta.dto.response.UserShortResponse;
import ru.secretsanta.entity.User;

public class UserMapper {
    public static UserShortResponse toUserShortResponse(User user) {
        return new UserShortResponse(
                user.getId(),
                user.getName(),
                user.getRole().toString());
    }
}
