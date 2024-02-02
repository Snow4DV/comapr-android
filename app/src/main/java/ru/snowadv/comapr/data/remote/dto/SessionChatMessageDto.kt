package ru.snowadv.comapr.data.remote.dto

import ru.snowadv.comapr.core.util.toZonedDateTimeWithCurrentZone
import ru.snowadv.comapr.domain.model.SessionChatMessage
import java.time.LocalDateTime


data class SessionChatMessageDto(
    val id: Long,
    val creator: UserDto,
    val timestamp: LocalDateTime,
    val text: String
) {
    fun toModel(): SessionChatMessage {
        return SessionChatMessage(id, creator.toModel(), timestamp.toZonedDateTimeWithCurrentZone(), text)
    }
}