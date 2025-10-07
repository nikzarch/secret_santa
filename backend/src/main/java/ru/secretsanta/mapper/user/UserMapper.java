package ru.secretsanta.mapper.user;

import ru.secretsanta.dto.response.UserShortResponse;
import ru.secretsanta.entity.user.User;

public class UserMapper {
    public static UserShortResponse toUserShortResponse(User user) {
        return new UserShortResponse(
                user.getId(),
                user.getName(),
                user.getRole().toString());
    }
}
