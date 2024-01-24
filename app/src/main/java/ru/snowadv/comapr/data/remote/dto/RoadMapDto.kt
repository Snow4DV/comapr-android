package ru.snowadv.comapr.data.remote.dto

import ru.snowadv.comapr.domain.model.RoadMap

data class RoadMapDto(
    val id: Long,
    val name: String,
    val description: String,
    val categoryId: Long,
    val nodes: List<NodeDto>,
    val likes: Int,
    val dislikes: Int,
    val categoryName: String
) {
    fun toModel(): RoadMap {
        return RoadMap(
            id,
            name,
            description,
            categoryId,
            nodes.map { it.toModel() },
            likes,
            dislikes,
            categoryName
        )
    }
}