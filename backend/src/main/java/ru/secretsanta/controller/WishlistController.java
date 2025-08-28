package ru.secretsanta.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.secretsanta.entity.WishlistItem;
import ru.secretsanta.service.WishlistService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;

    @PostMapping
    public ResponseEntity<String> addItem(Authentication authentication,
                                          @RequestBody WishlistItem item) {
        String username = authentication.getName();
        wishlistService.addItem(username, item);
        return ResponseEntity.ok("Item added");
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<String> updateItem(@PathVariable Long itemId,
                                             @RequestBody WishlistItem item) {
        wishlistService.updateItem(itemId, item);
        return ResponseEntity.ok("Item updated");
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteItem(@PathVariable Long itemId) {
        wishlistService.deleteItem(itemId);
        return ResponseEntity.ok("Item deleted");
    }

    @GetMapping
    public ResponseEntity<List<WishlistItem>> getWishlist(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(wishlistService.getWishlist(username));
    }
}
