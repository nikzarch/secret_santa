package ru.secretsanta.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateEventRequest(
        @NotBlank String name,
        String description,
        @NotNull LocalDate date,

        @JsonProperty("group_id")
        Long groupId
) {}
