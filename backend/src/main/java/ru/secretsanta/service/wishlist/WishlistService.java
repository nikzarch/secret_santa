package ru.secretsanta.service.wishlist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.secretsanta.dto.response.WishlistItemResponse;
import ru.secretsanta.entity.wishlist.WishlistItem;

public interface WishlistService {
    WishlistItemResponse addItem(Long userId, WishlistItem item);

    WishlistItemResponse updateItem(Long userId, Long itemId, WishlistItem updatedItem);

    void deleteItem(Long userId, Long itemId);

    Page<WishlistItemResponse> getWishlist(Long userId, Pageable pageable);
}
