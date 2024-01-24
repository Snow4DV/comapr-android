package ru.snowadv.comapr.domain.model

import ru.snowadv.comapr.data.remote.dto.LoginInfoDto


data class LoginInfo(val username: String, val password: String) {
    fun toDto(): LoginInfoDto {
        return LoginInfoDto(username, password)
    }
}