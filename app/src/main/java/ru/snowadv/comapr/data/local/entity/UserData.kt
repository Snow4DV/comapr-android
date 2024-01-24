package ru.snowadv.comapr.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.snowadv.comapr.domain.model.AuthUser

@Entity
data class UserData(
    @PrimaryKey var id: Long? = null,
    val token: String,
    val username: String,
    val email: String,
    val roles: List<String>
) {
    fun toModel(): AuthUser {
        return AuthUser(id, token, username, email, roles)
    }
}