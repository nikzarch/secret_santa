package ru.secretsanta.dto.response;

public record UserShortResponse(
        Long id,
        String name,
        String role
) {}
