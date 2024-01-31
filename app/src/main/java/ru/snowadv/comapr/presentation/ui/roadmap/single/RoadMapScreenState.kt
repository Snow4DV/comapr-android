package ru.snowadv.comapr.presentation.ui.roadmap.single

import ru.snowadv.comapr.domain.model.RoadMap

data class RoadMapScreenState(
    val roadMap: RoadMap? = null,
    val loading: Boolean = true
)