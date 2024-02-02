package ru.snowadv.comaprbackend.dto

import ru.snowadv.comapr.domain.model.RoadMapItem

data class SimpleRoadMapDto(
    val id: Long,
    val name: String
) {

    fun toModel(): RoadMapItem {
        return RoadMapItem(
            id = id,
            name = name
        )
    }
}