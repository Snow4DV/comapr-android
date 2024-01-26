package ru.snowadv.comapr.presentation.screen.roadmap.single

import ru.snowadv.comapr.domain.model.Category
import ru.snowadv.comapr.domain.model.RoadMap
import ru.snowadv.comaprbackend.dto.CategorizedRoadMaps

data class RoadMapScreenState(
    val roadMap: RoadMap? = null,
    val loading: Boolean = true
)