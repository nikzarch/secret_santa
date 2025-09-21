package ru.secretsanta.service;

import ru.secretsanta.entity.User;

public interface UserService {
    User getUserByName(String name);
    void deleteUserByName(String name);
}
