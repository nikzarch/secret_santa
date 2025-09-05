package ru.secretsanta.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.secretsanta.entity.User;
import ru.secretsanta.entity.WishlistItem;
import ru.secretsanta.repository.UserRepository;
import ru.secretsanta.repository.WishlistRepository;
import ru.secretsanta.service.WishlistService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;

    @Override
    public WishlistItem addItem(String username, WishlistItem item) {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        item.setUser(user);
        return wishlistRepository.save(item);
    }

    @Override
    public WishlistItem updateItem(String username, Long itemId, WishlistItem updatedItem) {
        WishlistItem existing = wishlistRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        existing.setTitle(updatedItem.getTitle());
        existing.setDescription(updatedItem.getDescription());
        return wishlistRepository.save(existing);
    }

    @Override
    public void deleteItem(Long itemId) {
        wishlistRepository.deleteById(itemId);
    }

    @Override
    public List<WishlistItem> getWishlist(String username) {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return wishlistRepository.findByUser(user);
    }
}
