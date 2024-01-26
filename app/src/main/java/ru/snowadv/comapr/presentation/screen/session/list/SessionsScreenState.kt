package ru.snowadv.comapr.presentation.screen.session.list

import ru.snowadv.comapr.domain.model.MapSession
import ru.snowadv.comapr.domain.model.RoadMap

data class SessionsScreenState(
    val sessions: List<MapSession> = emptyList(),
    val loading: Boolean = true,
)