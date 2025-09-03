package ru.secretsanta.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.secretsanta.dto.request.WishlistItemRequest;
import ru.secretsanta.dto.response.WishlistItemResponse;
import ru.secretsanta.entity.WishlistItem;
import ru.secretsanta.mapper.WishlistMapper;
import ru.secretsanta.service.WishlistService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;

    @PostMapping
    public ResponseEntity<Map<String,String>> addItem(
            Authentication authentication,
            @Valid @RequestBody WishlistItemRequest item
    ) {
        String username = authentication.getName();
        WishlistItem wishlistItem = WishlistMapper.toWishlistItemRequest(item);
        wishlistService.addItem(username,wishlistItem );
        return ResponseEntity.ok(Map.of("message","Item added"));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<Map<String,String>> updateItem(
            @PathVariable Long itemId,
            @Valid @RequestBody WishlistItemRequest item
    ) {
        WishlistItem wishlistItem = WishlistMapper.toWishlistItemRequest(item);
        wishlistService.updateItem(itemId, wishlistItem);
        return ResponseEntity.ok(Map.of("message","Item updated"));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Map<String,String>> deleteItem(@PathVariable Long itemId) {
        wishlistService.deleteItem(itemId);
        return ResponseEntity.ok(Map.of("message","Item deleted"));
    }

    @GetMapping
    public ResponseEntity<List<WishlistItemResponse>> getWishlist(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(wishlistService.getWishlist(username).stream().map(WishlistMapper::toDto).toList());
    }
}
