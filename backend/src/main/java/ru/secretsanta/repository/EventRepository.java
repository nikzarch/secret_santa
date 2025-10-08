package ru.secretsanta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.secretsanta.entity.event.Event;
import ru.secretsanta.entity.user.User;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findById(Long id);

    Optional<Event> findByName(String name);
    Page<Event> findByIsActiveTrue(Pageable pageable);

    Page<Event> findAll(Pageable pageable);

    Page<Event> findByParticipantsContaining(User user, Pageable pageable);
}
