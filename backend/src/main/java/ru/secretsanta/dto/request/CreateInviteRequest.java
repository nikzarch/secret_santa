package ru.secretsanta.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateInviteRequest(
        @JsonProperty("group_id")
        Long groupId,
        String username) {}