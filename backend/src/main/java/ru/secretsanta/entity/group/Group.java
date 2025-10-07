package ru.secretsanta.entity.group;

import jakarta.persistence.*;
import lombok.*;
import ru.secretsanta.entity.user.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups",
        uniqueConstraints = @UniqueConstraint(columnNames = {"owner_id", "name"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @ManyToOne
    private User owner;

    @ManyToMany
    @Builder.Default
    private Set<User> users = new HashSet<>();

    @Builder.Default
    private LocalDate createdAt = LocalDate.now();

}
