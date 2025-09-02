package ru.secretsanta.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String salt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @ManyToMany(mappedBy = "participants")
    @Builder.Default
    private List<Event> events = new ArrayList<>();

    @OneToMany(mappedBy = "santa", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SantaAssignment> sentAssignments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SantaAssignment> receivedAssignments = new ArrayList<>();
}
