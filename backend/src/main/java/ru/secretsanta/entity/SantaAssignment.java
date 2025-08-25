package ru.secretsanta.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "santa_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SantaAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(optional = false)
    @JoinColumn(name = "santa_participant_id")
    private User santa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_participant_id")
    private User receiver;
}
