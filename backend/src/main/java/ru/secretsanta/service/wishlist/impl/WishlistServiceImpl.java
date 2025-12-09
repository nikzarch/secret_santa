package ru.secretsanta.service.wishlist.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.secretsanta.dto.response.WishlistItemResponse;
import ru.secretsanta.entity.user.User;
import ru.secretsanta.entity.wishlist.WishlistItem;
import ru.secretsanta.exception.common.NotFoundException;
import ru.secretsanta.exception.common.TooMuchItemsException;
import ru.secretsanta.exception.user.InsufficientPrivilegesException;
import ru.secretsanta.mapper.wishlist.WishlistMapper;
import ru.secretsanta.repository.UserRepository;
import ru.secretsanta.repository.WishlistRepository;
import ru.secretsanta.service.wishlist.WishlistService;

import java.util.List;

import static ru.secretsanta.util.AppConstants.MAX_ITEMS_FOR_USER;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;


    @Override
    public WishlistItemResponse addItem(Long userId, WishlistItem item) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        item.setUser(user);
        List<WishlistItem> wishlistItemsOfUser = wishlistRepository.findByUser(user,Pageable.unpaged()).getContent();
        if (wishlistItemsOfUser.size() > MAX_ITEMS_FOR_USER){
            throw new TooMuchItemsException("too much wishlist items for " + user.getName());
        }
        return WishlistMapper.toWishlistItemResponse(wishlistRepository.save(item));
    }

    @Override
    public WishlistItemResponse updateItem(Long userId, Long itemId, WishlistItem updatedItem) {
        WishlistItem existing = wishlistRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        if (!existing.getUser().getId().equals(userId)){
            throw new InsufficientPrivilegesException("you have no rights to change this item");
        }
        existing.setTitle(updatedItem.getTitle());
        existing.setDescription(updatedItem.getDescription());
        existing.setPriority(updatedItem.getPriority());
        existing.setLink(updatedItem.getLink());
        return WishlistMapper.toWishlistItemResponse(wishlistRepository.save(existing));
    }

    @Override
    public void deleteItem(Long userId, Long itemId) {
        WishlistItem existing = wishlistRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        if (!existing.getUser().getId().equals(userId)){
            throw new InsufficientPrivilegesException("you have no rights to delete this item");
        }
        wishlistRepository.deleteById(itemId);
    }

    @Override
    public Page<WishlistItemResponse> getWishlist(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return wishlistRepository.findByUser(user,pageable).map(WishlistMapper::toWishlistItemResponse);
    }
}
