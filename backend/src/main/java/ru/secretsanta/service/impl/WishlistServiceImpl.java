package ru.secretsanta.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.secretsanta.dto.response.WishlistItemResponse;
import ru.secretsanta.entity.User;
import ru.secretsanta.entity.WishlistItem;
import ru.secretsanta.exception.TooMuchWishlistItemsForUserException;
import ru.secretsanta.exception.UserNotFoundException;
import ru.secretsanta.mapper.WishlistMapper;
import ru.secretsanta.repository.UserRepository;
import ru.secretsanta.repository.WishlistRepository;
import ru.secretsanta.service.WishlistService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final int MAX_ITEMS_FOR_USER = 25;

    @Override
    public WishlistItemResponse addItem(String username, WishlistItem item) {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        item.setUser(user);
        List<WishlistItem> wishlistItemsOfUser = wishlistRepository.findByUser(user);
        if (wishlistItemsOfUser.size() > MAX_ITEMS_FOR_USER){
            throw new TooMuchWishlistItemsForUserException("too much wishlist items for" + user.getName());
        }
        return WishlistMapper.toWishlistItemResponse(wishlistRepository.save(item));
    }

    @Override
    public WishlistItemResponse updateItem(String username, Long itemId, WishlistItem updatedItem) {
        WishlistItem existing = wishlistRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        existing.setTitle(updatedItem.getTitle());
        existing.setDescription(updatedItem.getDescription());
        return WishlistMapper.toWishlistItemResponse(wishlistRepository.save(existing));
    }

    @Override
    public void deleteItem(Long itemId) {
        wishlistRepository.deleteById(itemId);
    }

    @Override
    public List<WishlistItemResponse> getWishlist(String username) {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return wishlistRepository.findByUser(user).stream().map(WishlistMapper::toWishlistItemResponse).toList();
    }
}
