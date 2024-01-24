package ru.snowadv.comapr.data.remote.dto

import ru.snowadv.comapr.domain.model.ClearMapSession
import java.time.LocalDateTime

data class ClearMapSessionDto(
    val public: Boolean,
    val startDate: LocalDateTime,
    val groupChatUrl: String?,
    val roadMapId: Long
) {
    fun toModel(): ClearMapSession {
        return ClearMapSession(public, startDate, groupChatUrl, roadMapId)
    }
}