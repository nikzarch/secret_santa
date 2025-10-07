package ru.secretsanta.entity.event;

import jakarta.persistence.*;
import lombok.*;
import ru.secretsanta.entity.group.Group;
import ru.secretsanta.entity.user.User;

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

    @ManyToOne
    private Group group;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SantaAssignment> assignments = new ArrayList<>();
}
