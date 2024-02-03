package ru.snowadv.comapr.presentation.ui.session.single

import ru.snowadv.comapr.domain.model.MapSession
import ru.snowadv.comapr.domain.model.RoadMap

data class SessionScreenState(
    val session: MapSession? = null,
    val loading: Boolean = true,
    val joined: Boolean = false
)