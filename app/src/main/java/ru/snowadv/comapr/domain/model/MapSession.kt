package ru.snowadv.comapr.domain.model

import java.time.LocalDateTime

data class MapSession(
    val id: Long,
    val creator: User,
    val users: List<UserMapCompletionState>,
    val public: Boolean,
    val startDate: LocalDateTime,
    val state: State,
    val groupChatUrl: String?,
    val messages: List<SessionChatMessage>,
    val roadMap: RoadMap
) {

    enum class State(val id: Int) {
        LOBBY(0), STARTED(1), FINISHED(2);

        companion object {
            fun getById(id: Int): State {
                return entries[id]
            }
        }
    }
}