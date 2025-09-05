package ru.secretsanta.service;

import ru.secretsanta.entity.WishlistItem;

import java.util.List;

public interface WishlistService {
    WishlistItem addItem(String username, WishlistItem item);

    WishlistItem updateItem(String username, Long itemId, WishlistItem updatedItem);

    void deleteItem(Long itemId);

    List<WishlistItem> getWishlist(String username);
}
