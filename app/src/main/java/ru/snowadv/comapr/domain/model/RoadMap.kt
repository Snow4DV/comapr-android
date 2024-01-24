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
    val categoryName: String,
    val verificationStatus: VerificationStatus
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
            categoryName,
            verificationStatus.id
        )
    }



    enum class VerificationStatus(val id: Int) {
        HIDDEN(0), UNVERIFIED(1), COMMUNITY_CHOICE(2), VERIFIED(3);
        companion object {
            fun fromId(id: Int): VerificationStatus {
                return entries.find { it.id == id } ?: error("no status with id $id")
            }
        }
    }
}