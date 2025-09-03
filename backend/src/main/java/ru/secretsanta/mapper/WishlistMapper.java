package ru.secretsanta.mapper;

import ru.secretsanta.dto.request.WishlistItemRequest;
import ru.secretsanta.entity.WishlistItem;
import ru.secretsanta.dto.response.WishlistItemResponse;

public class WishlistMapper {

    public static WishlistItem toWishlistItemRequest(WishlistItemRequest dto) {
        if (dto == null) return null;
        WishlistItem item = new WishlistItem();
        item.setTitle(dto.name());
        item.setDescription(dto.description());
        return item;
    }

    public static WishlistItemResponse toDto(WishlistItem entity) {
        if (entity == null) return null;
        return new WishlistItemResponse(entity.getId(), entity.getTitle(), entity.getDescription());
    }
}
