package ru.snowadv.comapr.data.remote.dto

import ru.snowadv.comapr.domain.model.UserMapCompletionState


data class UserMapCompletionStateDto(
    val id: Long,
    val user: UserDto,
    val tasksStates: List<UserTaskCompletionStateDto>
) {
    fun toModel(): UserMapCompletionState {
        return UserMapCompletionState(id, user.toModel(), tasksStates.filter { it.state }.map { it.taskId })
    }
}