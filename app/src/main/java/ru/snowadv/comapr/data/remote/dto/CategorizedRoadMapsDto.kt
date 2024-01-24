package ru.snowadv.comaprbackend.dto

import ru.snowadv.comapr.data.remote.dto.RoadMapDto

class CategorizedRoadMapsDto(
    val categoryName: String,
    val categoryId: Long,
    val roadMaps: List<RoadMapDto>
) {
    fun toModel(): CategorizedRoadMaps {
        return CategorizedRoadMaps(categoryName,  categoryId, roadMaps.map { it.toModel() })
    }
}