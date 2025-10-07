

package ru.secretsanta.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record GroupInviteResponse(
        Long id,
        @JsonProperty("group_id")
        Long groupId,
        @JsonProperty("group_name")
        String groupName,
        @JsonProperty("inviter_id")
        Long inviterId,
        @JsonProperty("inviter_name")
        String inviterName,
        @JsonProperty("invitee_id")
        Long inviteeId,
        @JsonProperty("invitee_name")
        String inviteeName,
        String status,
        @JsonProperty("created_at")
        Instant createdAt
) {}