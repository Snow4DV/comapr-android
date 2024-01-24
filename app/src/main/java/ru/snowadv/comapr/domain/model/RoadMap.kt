package ru.snowadv.comapr.domain.model

import androidx.compose.runtime.Composable
import ru.snowadv.comapr.R
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


    enum class VerificationStatus(val id: Int, val iconResId: Int) {
        HIDDEN(0, R.drawable.hidden_filled),
        UNVERIFIED(1, R.drawable.unverified_filled),
        COMMUNITY_CHOICE(2, R.drawable.community_choice_filled),
        VERIFIED(3, R.drawable.verified_filled);

        companion object {
            fun fromId(id: Int): VerificationStatus {
                return entries.find { it.id == id } ?: error("no status with id $id")
            }
        }
    }
}