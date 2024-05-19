package ru.snowadv.comapr.data.remote.dto

import ru.snowadv.comaprbackend.dto.CategorizedRoadMaps

class CategorizedRoadMapsDto(
    val categoryName: String,
    val categoryId: Long,
    val roadMaps: List<RoadMapDto>
) {
    fun toModel(): CategorizedRoadMaps {
        return CategorizedRoadMaps(categoryName,  categoryId, roadMaps.map { it.toModel() })
    }
}