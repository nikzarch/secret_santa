package ru.secretsanta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.secretsanta.entity.WishlistItem;
import ru.secretsanta.entity.User;

import java.util.List;

public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByUser(User user);
}
