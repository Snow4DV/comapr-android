package ru.snowadv.comapr.domain.model

data class UserMapCompletionState(
    val id: Long,
    val user: User,
    val finishedTasksIds: Set<Long>
)