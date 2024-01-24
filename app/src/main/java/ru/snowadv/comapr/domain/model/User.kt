package ru.snowadv.comapr.domain.model

import ru.snowadv.comapr.data.remote.dto.UserDto


data class User(
    val id: Long,
    val username: String,
    val email: String,
    val role: String
) {
    fun toDto(): UserDto {
        return UserDto(id, username, email, role)
    }
}