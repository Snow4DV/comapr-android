package ru.snowadv.comapr.data.remote.dto

import ru.snowadv.comapr.domain.model.LoginInfo


data class LoginInfoDto(val username: String, val password: String) {
    fun toModel(): LoginInfo {
        return LoginInfo(username, password)
    }
}