package ru.snowadv.comapr.domain.model

data class Task(
    val id: Long,
    val name: String,
    val description: String,
    val url: String?,
    val finishedUserIds: List<Long>? = null,
    val challenges: List<Challenge> = emptyList(),
)
