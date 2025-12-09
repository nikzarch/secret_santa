package ru.secretsanta.mapper.wishlist;

import ru.secretsanta.dto.request.WishlistItemRequest;
import ru.secretsanta.dto.response.WishlistItemResponse;
import ru.secretsanta.entity.wishlist.WishlistItem;

public class WishlistMapper {

    public static WishlistItem toWishlistItemRequest(WishlistItemRequest dto) {
        if (dto == null) return null;
        WishlistItem item = new WishlistItem();
        item.setTitle(dto.name());
        item.setDescription(dto.description());
        item.setLink(dto.link());
        item.setPriority(dto.priority());
        return item;
    }

    public static WishlistItemResponse toWishlistItemResponse(WishlistItem entity) {
        if (entity == null) return null;
        return new WishlistItemResponse(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getLink(), entity.getPriority());
    }

    public static WishlistItem toWishlistItem(WishlistItemRequest request) {
        return WishlistItem.builder()
                .title(request.name())
                .description(request.description())
                .link(request.link())
                .priority(request.priority())
                .build();
    }
}
