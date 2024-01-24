package ru.snowadv.comapr.data.remote.dto

import ru.snowadv.comapr.domain.model.MapSession
import java.time.LocalDateTime

data class MapSessionDto(
    val id: Long,
    val creator: UserDto,
    val users: List<UserMapCompletionStateDto>,
    val public: Boolean,
    val startDate: LocalDateTime,
    val stateId: Int,
    val groupChatUrl: String?,
    val messages: List<SessionChatMessageDto>,
    val roadMap: RoadMapDto
) {
    fun toModel(): MapSession {
        return MapSession(
            id,
            creator.toModel(),
            users.map { it.toModel() },
            public,
            startDate,
            MapSession.State.getById(stateId),
            groupChatUrl,
            messages.map { it.toModel() },
            roadMap.toModel()
        )
    }
}