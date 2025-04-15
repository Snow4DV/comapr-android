package ru.snowadv.comapr.domain.model


data class User(
    val id: Long,
    val username: String,
    val email: String,
    val role: Role
)
