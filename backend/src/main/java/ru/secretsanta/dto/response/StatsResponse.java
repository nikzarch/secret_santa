package ru.secretsanta.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StatsResponse (
        @JsonProperty("number_of_registered_users")
     Integer numberOfRegisteredUsers,
        @JsonProperty("number_of_active_events")
     Integer numberOfActiveEvents,
        @JsonProperty("number_of_events")
     Integer numberOfEvents,
        @JsonProperty("number_of_groups")
     Integer numberOfGroups){}
