package ru.secretsanta.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.secretsanta.dto.request.WishlistItemRequest;
import ru.secretsanta.dto.response.GenericResponse;
import ru.secretsanta.dto.response.WishlistItemResponse;
import ru.secretsanta.mapper.WishlistMapper;
import ru.secretsanta.service.WishlistService;

import java.util.List;


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
        WishlistItemResponse saved = wishlistService.addItem(username, WishlistMapper.toWishlistItem(request));
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<WishlistItemResponse> updateItem(
            Authentication authentication,
            @PathVariable Long itemId,
            @Valid @RequestBody WishlistItemRequest request
    ) {
        String username = authentication.getName();
        WishlistItemResponse updated = wishlistService.updateItem(username, itemId, WishlistMapper.toWishlistItemRequest(request));
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<GenericResponse> deleteItem(@PathVariable Long itemId) {
        wishlistService.deleteItem(itemId);
        return ResponseEntity.ok(new GenericResponse("Item deleted"));
    }

    @GetMapping
    public ResponseEntity<List<WishlistItemResponse>> getWishlist(Authentication authentication) {
        String username = authentication.getName();
        List<WishlistItemResponse> items = wishlistService.getWishlist(username);
        return ResponseEntity.ok(items);
    }
}
