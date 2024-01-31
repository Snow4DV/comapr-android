package ru.snowadv.comapr.presentation.ui.create_edit_session

import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.TemporalAmount

data class CreateEditSessionState(
    val loading: Boolean = false,
    val public: Boolean = true,
    val startDate: LocalDateTime = LocalDateTime.now() + Duration.ofDays(2),
    val groupChatUrl: String = "",
    val roadMapId: Long? = null,
    val invalidRoadMap: Boolean = false
) {
    val validRoadMap
        get() = roadMapId != null
}