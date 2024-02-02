package ru.snowadv.comapr.presentation.ui.session.modify

import ru.snowadv.comapr.domain.model.RoadMapItem
import java.time.Duration
import java.time.ZonedDateTime

data class CreateEditSessionScreenState(
    val newSession: Boolean = true,
    val loading: Boolean = false,
    val public: Boolean = true,
    val startDate: ZonedDateTime = ZonedDateTime.now() + Duration.ofDays(2),
    val groupChatUrl: String = "",
    val roadMapId: Long? = null,
    val roadMaps: Map<Long, RoadMapItem> = emptyMap()
) {
    val validRoadMap
        get() = roadMapId != null
}