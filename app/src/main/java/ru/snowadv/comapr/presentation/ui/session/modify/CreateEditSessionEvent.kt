package ru.snowadv.comapr.presentation.ui.session.modify

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.snowadv.comapr.domain.repository.DataRepository
import java.time.LocalDateTime
import java.time.ZonedDateTime


sealed class CreateEditSessionEvent {
    data class ChangedPublic(val value: Boolean) : CreateEditSessionEvent()
    data class ChangedStartDate(val date: ZonedDateTime) : CreateEditSessionEvent()
    data class ChangedGroupChatUrl(val url: String) : CreateEditSessionEvent()
    data class ChangedRoadMapId(val id: Long?) : CreateEditSessionEvent()

}