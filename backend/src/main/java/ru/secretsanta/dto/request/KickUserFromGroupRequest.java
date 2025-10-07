package ru.secretsanta.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record KickUserFromGroupRequest(
        @JsonProperty("group_name")
        @NotBlank
        String groupName,

        @NotBlank
        String username) {
}
