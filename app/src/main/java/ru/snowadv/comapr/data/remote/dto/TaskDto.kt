package ru.snowadv.comapr.data.remote.dto

import ru.snowadv.comapr.domain.model.Task

data class TaskDto(
    val id: Long,
    val name: String,
    val description: String,
    val url: String?,
    val finishedUserIds: List<Long>? = null
) {
    fun toModel(): Task {
        return Task(id, name, description, url, finishedUserIds)
    }
}