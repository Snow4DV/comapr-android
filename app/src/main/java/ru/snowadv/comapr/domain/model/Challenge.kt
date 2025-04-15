package ru.snowadv.comapr.domain.model

data class Challenge(
    val id: Long,
    val description: String,
    val answers: List<String>,
)
