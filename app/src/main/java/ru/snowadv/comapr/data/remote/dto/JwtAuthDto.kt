package ru.snowadv.comapr.data.remote.dto

import ru.snowadv.comapr.domain.model.JwtAuth


data class JwtAuthDto(
    val accessToken: String,
    val id: Long,
    val username: String,
    val email: String,
    val roles: List<String>
) {
    fun toModel(): JwtAuth {
        return JwtAuth(accessToken, id, username, email, roles)
    }
}