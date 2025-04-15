package ru.snowadv.comapr.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.data.remote.dto.NewSessionChatMessageDto
import ru.snowadv.comapr.domain.model.MapSession
import ru.snowadv.comapr.domain.model.SessionChatMessage
import java.time.ZonedDateTime

interface SessionRepository {
    fun fetchSessions(): Flow<Resource<List<MapSession>>>
    fun fetchActiveSessionsByUser(): Flow<Resource<List<MapSession>>>
    fun getSession(id: Long): Flow<Resource<MapSession>>

    fun createSession(
        public: Boolean,
        startDate: ZonedDateTime,
        groupChatUrl: String?,
        roadMapId: Long
    ): Flow<Resource<MapSession>>

    fun updateSession(
        public: Boolean,
        startDate: ZonedDateTime,
        groupChatUrl: String?,
        roadMapId: Long,
        id: Long
    ): Flow<Resource<MapSession>>

    fun startSession(id: Long): Flow<Resource<MapSession>>
    fun endSession(id: Long): Flow<Resource<MapSession>>
    fun joinSession(id: Long): Flow<Resource<MapSession>>
    fun leaveSession(id: Long): Flow<Resource<MapSession>>

    fun sendMessage(
        id: Long,
        message: NewSessionChatMessageDto
    ): Flow<Resource<List<SessionChatMessage>>>

    fun markTask(
        id: Long,
        taskId: Long,
        state: Boolean
    ): Flow<Resource<MapSession>>
}

