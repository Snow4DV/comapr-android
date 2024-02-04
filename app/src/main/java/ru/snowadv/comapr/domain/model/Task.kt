package ru.snowadv.comapr.domain.model

import ru.snowadv.comapr.data.remote.dto.TaskDto

data class Task(
    val id: Long,
    val name: String,
    val description: String,
    val url: String?,
    val finishedUserIds: List<Long>? = null
) {
    fun toDto(): TaskDto {
        return TaskDto(id, name, description, url)
    }
}