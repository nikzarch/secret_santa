package ru.secretsanta.dto.response;

import java.util.List;

public record GroupResponse(Long id,String name,UserShortResponse owner, List<UserShortResponse> users) {
}
