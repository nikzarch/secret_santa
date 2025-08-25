package ru.secretsanta.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AddEventRequest(
        @NotBlank String name,
        String description,
        @NotNull LocalDate date) {}
