package ru.secretsanta.service.user;

import ru.secretsanta.dto.response.UserShortResponse;
import ru.secretsanta.entity.user.User;

public interface UserService {
    UserShortResponse getUserByName(String name);
    void deleteUserByName(String name);
    User getCurrentUser();

    Integer getUsersRegistered();
}
