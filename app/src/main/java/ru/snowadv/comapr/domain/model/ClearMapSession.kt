package ru.snowadv.comapr.domain.model

import ru.snowadv.comapr.data.remote.dto.ClearMapSessionDto
import java.time.LocalDateTime

data class ClearMapSession(
    val public: Boolean,
    val startDate: LocalDateTime,
    val groupChatUrl: String?,
    val roadMapId: Long
) {
    fun toDto(): ClearMapSessionDto {
        return ClearMapSessionDto(public, startDate, groupChatUrl, roadMapId)
    }
}