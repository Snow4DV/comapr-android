package ru.snowadv.comapr.domain.model


data class AuthUser(
    val id: Long?,
    val token: String,
    val username: String,
    val email: String,
    val roles: List<String>
)