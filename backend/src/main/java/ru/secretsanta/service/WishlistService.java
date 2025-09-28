package ru.secretsanta.service;

import ru.secretsanta.dto.response.WishlistItemResponse;
import ru.secretsanta.entity.WishlistItem;

import java.util.List;

public interface WishlistService {
    WishlistItemResponse addItem(String username, WishlistItem item);

    WishlistItemResponse updateItem(String username, Long itemId, WishlistItem updatedItem);

    void deleteItem(Long itemId);

    List<WishlistItemResponse> getWishlist(String username);
}
