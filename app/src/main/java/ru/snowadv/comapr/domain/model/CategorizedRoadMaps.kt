package ru.snowadv.comapr.domain.model

class CategorizedRoadMaps(
    val categoryName: String,
    val categoryId: Long,
    val roadMaps: List<RoadMap>
)