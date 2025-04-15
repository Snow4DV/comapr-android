package ru.snowadv.comapr.domain.model

data class Node(
    val id: Long,
    val name: String,
    var description: String?,
    val tasks: List<Task>
)