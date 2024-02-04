package ru.snowadv.comapr.domain.model

import java.time.LocalDateTime
import java.time.ZonedDateTime

data class MapSession(
    val id: Long,
    val creator: User,
    val users: List<UserMapCompletionState>,
    val public: Boolean,
    val startDate: ZonedDateTime,
    val state: State,
    val groupChatUrl: String?,
    val messages: List<SessionChatMessage>,
    val roadMap: RoadMap,
    val joined: Boolean,
    val isCreator: Boolean,
    val finishedTasksIds: List<Long>
) {

    enum class State(val id: Int) {
        LOBBY(0), STARTED(1), FINISHED(2);

        companion object {
            fun getById(id: Int): State {
                return entries[id]
            }
        }
    }


    fun toClearModel(): ClearMapSession {
        return ClearMapSession(
            public = public,
            startDate = startDate,
            groupChatUrl = groupChatUrl,
            roadMapId = roadMap.id
        )
    }
}

