package ru.secretsanta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.secretsanta.entity.Event;
import ru.secretsanta.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findById(Long id);

    Optional<Event> findByName(String name);

    Page<Event> findAll(Pageable pageable);


    @Query("SELECT e FROM Event e JOIN e.participants u WHERE u = :user")
    Page<Event> findAllByParticipant(@Param("user") User user, Pageable pageable);


    Page<Event> findByParticipantsContaining(User user, Pageable pageable);

}
