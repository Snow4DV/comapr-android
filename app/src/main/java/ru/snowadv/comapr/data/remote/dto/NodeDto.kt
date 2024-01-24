package ru.snowadv.comapr.data.remote.dto

import ru.snowadv.comapr.domain.model.Node

data class NodeDto(
    val id: Long,
    val name: String,
    var description: String?,
    val tasks: List<TaskDto>
) {
    fun toModel(): Node {
        return Node(id, name, description, tasks.map { it.toModel() })
    }
}