package ru.secretsanta.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AddUserToGroupRequest(
        @JsonProperty("group_name")
        String groupName,

        String username) {
}

