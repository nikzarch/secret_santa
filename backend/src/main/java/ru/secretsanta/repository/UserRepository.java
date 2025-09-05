package ru.secretsanta.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.secretsanta.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);

    Optional<User> findById(Long id);

    boolean existsByName(String name);
}