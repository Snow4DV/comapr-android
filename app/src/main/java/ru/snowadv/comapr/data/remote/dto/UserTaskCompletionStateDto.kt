package ru.snowadv.comapr.data.remote.dto

data class UserTaskCompletionStateDto(
    var id: Long,
    val taskId: Long,
    var state: Boolean
)