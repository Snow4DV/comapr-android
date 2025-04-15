package ru.snowadv.comapr.domain.model

import ru.snowadv.comapr.data.remote.dto.SignupInfoDto

data class SignupInfo(
    val username: String,
    val email: String,
    val password: String
)
