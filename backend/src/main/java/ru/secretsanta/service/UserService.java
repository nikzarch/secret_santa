package ru.secretsanta.service;

import ru.secretsanta.dto.response.UserShortResponse;
import ru.secretsanta.entity.User;

public interface UserService {
    UserShortResponse getUserByName(String name);
    void deleteUserByName(String name);
}
