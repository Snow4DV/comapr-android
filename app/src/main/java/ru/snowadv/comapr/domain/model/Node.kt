package ru.snowadv.comapr.domain.model

import ru.snowadv.comapr.data.remote.dto.NodeDto

data class Node(
    val id: Long,
    val name: String,
    var description: String?,
    val tasks: List<Task>
) {
    fun toDto(): NodeDto {
        return NodeDto(id, name, description, tasks.map { it.toDto() })
    }
}