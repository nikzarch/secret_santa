package ru.secretsanta.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WishlistItemRequest(
        @NotBlank
        String name,

        String description
) {}
