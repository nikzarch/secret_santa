package ru.secretsanta.mapper.user

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import ru.secretsanta.dto.response.InviteResponse
import ru.secretsanta.entity.user.InviteToken

@Component
@EnableConfigurationProperties(InviteProperties::class)
class InviteMapper(
    private var properties: InviteProperties
) {
    fun toInviteResponse(inviteToken: InviteToken): InviteResponse {
        return InviteResponse(inviteToken.username, properties.host + "/register?token=" + inviteToken.token)
    }
}

@ConfigurationProperties(prefix = "invites")
data class InviteProperties(
     var host: String = "")
