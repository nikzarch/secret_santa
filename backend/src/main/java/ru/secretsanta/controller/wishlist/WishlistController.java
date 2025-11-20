package ru.secretsanta.controller.wishlist;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.secretsanta.dto.request.WishlistItemRequest;
import ru.secretsanta.dto.response.ErrorResponse;
import ru.secretsanta.dto.response.GenericResponse;
import ru.secretsanta.dto.response.WishlistItemResponse;
import ru.secretsanta.entity.user.User;
import ru.secretsanta.exception.user.InsufficientPrivilegesException;
import ru.secretsanta.exception.user.InviteStatusException;
import ru.secretsanta.mapper.wishlist.WishlistMapper;
import ru.secretsanta.service.user.UserService;
import ru.secretsanta.service.wishlist.WishlistService;


@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<WishlistItemResponse> addItem(
            @Valid @RequestBody WishlistItemRequest request
    ) {
        User current = userService.getCurrentUser();
        WishlistItemResponse saved = wishlistService.addItem(current.getId(),WishlistMapper.toWishlistItem(request));
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<WishlistItemResponse> updateItem(
            @PathVariable Long itemId,
            @Valid @RequestBody WishlistItemRequest request
    ) {
        User current = userService.getCurrentUser();
        WishlistItemResponse updated = wishlistService.updateItem(current.getId(), itemId, WishlistMapper.toWishlistItemRequest(request));
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<GenericResponse> deleteItem(@PathVariable Long itemId) {
        User current = userService.getCurrentUser();
        wishlistService.deleteItem(current.getId(),itemId);
        return ResponseEntity.ok(new GenericResponse("Item deleted"));
    }

    @GetMapping
    public ResponseEntity<Page<WishlistItemResponse>> getWishlist(Pageable pageable) {
        User current = userService.getCurrentUser();
        return ResponseEntity.ok(wishlistService.getWishlist(current.getId(),pageable));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Page<WishlistItemResponse>> getWishlistOfUser(@PathVariable(name = "id") Long userId, Pageable pageable){
        return ResponseEntity.ok(wishlistService.getWishlist(userId,pageable));
    }

    @ExceptionHandler(InsufficientPrivilegesException.class)
    public ResponseEntity<ErrorResponse> handle(InsufficientPrivilegesException exc){
        return new ResponseEntity<>(new ErrorResponse(exc.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
