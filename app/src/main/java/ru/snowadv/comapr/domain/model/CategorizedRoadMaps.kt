package ru.snowadv.comaprbackend.dto

import ru.snowadv.comapr.data.remote.dto.RoadMapDto
import ru.snowadv.comapr.domain.model.RoadMap

class CategorizedRoadMaps(
    val categoryName: String,
    val categoryId: Long,
    val roadMaps: List<RoadMap>
)