package ru.secretsanta.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.secretsanta.dto.response.WishlistItemResponse;
import ru.secretsanta.entity.WishlistItem;

import java.util.List;

public interface WishlistService {
    WishlistItemResponse addItem(String username, WishlistItem item);

    WishlistItemResponse updateItem(String username, Long itemId, WishlistItem updatedItem);

    void deleteItem(Long itemId);

    Page<WishlistItemResponse> getWishlist(String username, Pageable pageable);
}
