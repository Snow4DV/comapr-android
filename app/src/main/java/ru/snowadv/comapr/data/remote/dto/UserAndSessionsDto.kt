package ru.snowadv.comapr.data.remote.dto

import ru.snowadv.comapr.domain.model.UserAndSessions

data class UserAndSessionsDto(
    val user: UserDto,
    val sessions: List<MapSessionDto>
) {
    fun toModel(): UserAndSessions {
        return UserAndSessions(user.toModel(), sessions.map { it.toModel() })
    }
}