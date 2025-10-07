package ru.secretsanta.entity.wishlist;

import jakarta.persistence.*;
import lombok.*;
import ru.secretsanta.entity.user.User;

@Entity
@Table(name = "wishlist_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String link;

    private int priority;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
}
