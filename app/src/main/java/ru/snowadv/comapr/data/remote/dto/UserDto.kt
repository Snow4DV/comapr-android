package ru.snowadv.comapr.data.remote.dto

import ru.snowadv.comapr.domain.model.User


data class UserDto(
    val id: Long,
    val username: String,
    val email: String,
    val role: String
) {
    fun toModel(): User {
        return User(id, username, email, role)
    }
}