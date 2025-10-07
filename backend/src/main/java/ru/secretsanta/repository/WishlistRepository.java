package ru.secretsanta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.secretsanta.entity.user.User;
import ru.secretsanta.entity.wishlist.WishlistItem;

public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {
    Page<WishlistItem> findByUser(User user, Pageable pageable);
}
