package ru.snowadv.comapr.domain.model

data class JwtAuth(
    val accessToken: String,
    val id: Long,
    val username: String,
    val email: String,
    val roles: List<String>
)
