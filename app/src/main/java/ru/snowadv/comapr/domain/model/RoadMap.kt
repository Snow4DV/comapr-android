package ru.snowadv.comapr.domain.model

import ru.snowadv.comapr.data.remote.dto.RoadMapDto

data class RoadMap(
    val id: Long,
    val name: String,
    val description: String,
    val categoryId: Long,
    val nodes: List<Node>,
    val likes: Int,
    val dislikes: Int,
    val categoryName: String
) {
    fun toDto(): RoadMapDto {
        return RoadMapDto(
            id,
            name,
            description,
            categoryId,
            nodes.map { it.toDto() },
            likes,
            dislikes,
            categoryName
        )
    }
}