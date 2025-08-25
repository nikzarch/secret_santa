package ru.secretsanta.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.secretsanta.entity.WishlistItem;
import ru.secretsanta.service.WishlistService;
import ru.secretsanta.util.JWTUtil;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;
    private final JWTUtil jwtUtil;

    @PostMapping
    public ResponseEntity<String> addItem(@RequestHeader("Authorization") String header,
                                          @RequestBody WishlistItem item) {
        String username = jwtUtil.extractUsername(header.substring(7));
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
    public ResponseEntity<List<WishlistItem>> getWishlist(@RequestHeader("Authorization") String header) {
        String username = jwtUtil.extractUsername(header.substring(7));
        return ResponseEntity.ok(wishlistService.getWishlist(username));
    }
}
