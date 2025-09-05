package ru.secretsanta.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    private boolean isActive;

    private boolean assignmentsGenerated;

    @ManyToMany
    @JoinTable(
            name = "event_users",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    private List<User> participants = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SantaAssignment> assignments = new ArrayList<>();
}
