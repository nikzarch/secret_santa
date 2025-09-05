package ru.secretsanta.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WishlistItemRequest(
        @NotBlank
        String name,
        String description,
        String link,
        @NotNull
        Integer priority
) {
}
