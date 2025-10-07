package ru.secretsanta.entity.user;

import jakarta.persistence.*;
import lombok.*;
import ru.secretsanta.entity.event.Event;
import ru.secretsanta.entity.event.SantaAssignment;
import ru.secretsanta.entity.group.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

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

    @ManyToMany(mappedBy = "users")
    @Builder.Default
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    @Builder.Default
    private Set<Group> groupsOwned = new HashSet<>();

    @OneToMany(mappedBy = "santa", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SantaAssignment> sentAssignments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SantaAssignment> receivedAssignments = new ArrayList<>();
}
