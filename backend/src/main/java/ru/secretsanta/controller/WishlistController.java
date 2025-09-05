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
    public ResponseEntity<WishlistItemResponse> addItem(
            Authentication authentication,
            @Valid @RequestBody WishlistItemRequest request
    ) {
        String username = authentication.getName();
        WishlistItem saved = wishlistService.addItem(username, WishlistMapper.toWishlistItem(request));
        return ResponseEntity.ok(WishlistMapper.toDto(saved));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<WishlistItemResponse> updateItem(
            Authentication authentication,
            @PathVariable Long itemId,
            @Valid @RequestBody WishlistItemRequest request
    ) {
        String username = authentication.getName();
        WishlistItem updated = wishlistService.updateItem(username, itemId, WishlistMapper.toWishlistItemRequest(request));
        return ResponseEntity.ok(WishlistMapper.toDto(updated));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Map<String,String>> deleteItem(@PathVariable Long itemId) {
        wishlistService.deleteItem(itemId);
        return ResponseEntity.ok(Map.of("message","Item deleted"));
    }

    @GetMapping
    public ResponseEntity<List<WishlistItemResponse>> getWishlist(Authentication authentication) {
        String username = authentication.getName();
        List<WishlistItem> items = wishlistService.getWishlist(username);
        List<WishlistItemResponse> dtoList = items.stream().map(WishlistMapper::toDto).toList();
        return ResponseEntity.ok(dtoList);
    }
}
