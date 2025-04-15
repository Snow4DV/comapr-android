package ru.snowadv.comapr.domain.model

import ru.snowadv.comapr.R

data class RoadMap(
    val id: Long,
    val name: String,
    val description: String,
    val categoryId: Long,
    val nodes: List<Node>,
    val likes: Int,
    val dislikes: Int,
    val categoryName: String,
    val verificationStatus: VerificationStatus,
    val tasksCount: Int
) {

    enum class VerificationStatus(val id: Int, val iconResId: Int, val textResId: Int) {
        HIDDEN(0, R.drawable.hidden_filled, R.string.hidden),
        UNVERIFIED(1, R.drawable.unverified_filled, R.string.unverified),
        COMMUNITY_CHOICE(2, R.drawable.community_choice_filled, R.string.community_choice),
        VERIFIED(3, R.drawable.verified_filled, R.string.verified);

        companion object {
            fun fromId(id: Int): VerificationStatus {
                return entries.find { it.id == id } ?: error("no status with id $id")
            }
        }
    }
}