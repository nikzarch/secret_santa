package ru.secretsanta.service;

import ru.secretsanta.entity.WishlistItem;
import java.util.List;

public interface WishlistService {
    void addItem(String username, WishlistItem item);
    void updateItem(Long itemId, WishlistItem updatedItem);
    void deleteItem(Long itemId);
    List<WishlistItem> getWishlist(String username);
}
