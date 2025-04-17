package ru.snowadv.comapr.data.remote.dto

import ru.snowadv.comapr.domain.model.Challenge

data class ChallengeDto(
    val id: Long,
    val description: String,
    val answers: List<String>,
) {
    fun toModel(): Challenge {
        return Challenge(id, description, answers)
    }
}
