package ru.secretsanta.dto.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WishlistItemRequest(
        @NotBlank
        String name,
        String description,
        String link,
        @NotNull
        @Max(10)
        @Min(1)
        Integer priority
) {
}
