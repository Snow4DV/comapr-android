package ru.snowadv.comapr.domain.model

import java.time.ZonedDateTime

data class ClearMapSession(
    val public: Boolean,
    val startDate: ZonedDateTime,
    val groupChatUrl: String?,
    val roadMapId: Long
)
