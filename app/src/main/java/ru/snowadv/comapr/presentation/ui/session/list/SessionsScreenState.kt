package ru.snowadv.comapr.presentation.ui.session.list

import ru.snowadv.comapr.domain.model.MapSession

data class SessionsScreenState(
    val sessions: List<MapSession> = emptyList(),
    val loading: Boolean = true,
)