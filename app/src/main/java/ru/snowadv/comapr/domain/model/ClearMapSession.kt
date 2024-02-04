package ru.snowadv.comapr.domain.model

import ru.snowadv.comapr.data.remote.dto.ClearMapSessionDto
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.TimeZone

data class ClearMapSession(
    val public: Boolean,
    val startDate: ZonedDateTime,
    val groupChatUrl: String?,
    val roadMapId: Long
) {
    fun toDto(): ClearMapSessionDto {
        return ClearMapSessionDto(public, startDate.toLocalDateTime(), if(groupChatUrl?.isBlank() != false) null else groupChatUrl, roadMapId)
    }
}