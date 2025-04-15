package ru.snowadv.comapr.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.core.util.safeApiCall
import ru.snowadv.comapr.core.util.toUtcLocalDateTime
import ru.snowadv.comapr.data.remote.ComaprApi
import ru.snowadv.comapr.data.remote.dto.ClearMapSessionDto
import ru.snowadv.comapr.data.remote.dto.NewSessionChatMessageDto
import ru.snowadv.comapr.domain.model.MapSession
import ru.snowadv.comapr.domain.model.SessionChatMessage
import ru.snowadv.comapr.domain.repository.SessionRepository
import java.time.ZonedDateTime

class SessionRepositoryImpl(
    private val api: ComaprApi
) : SessionRepository {

    override fun fetchSessions(): Flow<Resource<List<MapSession>>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.fetchSessions().map { it.toModel() }))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun fetchActiveSessionsByUser(): Flow<Resource<List<MapSession>>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.getActiveSessions().map { it.toModel() }))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun getSession(id: Long): Flow<Resource<MapSession>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.getSession(id).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun createSession(
        public: Boolean,
        startDate: ZonedDateTime,
        groupChatUrl: String?,
        roadMapId: Long
    ): Flow<Resource<MapSession>> = flow {
        val dto = ClearMapSessionDto(
            public,
            startDate.toUtcLocalDateTime(),
            groupChatUrl,
            roadMapId
        )
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.createSession(dto).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun updateSession(
        public: Boolean,
        startDate: ZonedDateTime,
        groupChatUrl: String?,
        roadMapId: Long,
        id: Long
    ): Flow<Resource<MapSession>> = flow {
        val dto = ClearMapSessionDto(
            public,
            startDate.toUtcLocalDateTime(),
            groupChatUrl,
            roadMapId
        )
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.updateSession(dto, id).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun startSession(id: Long): Flow<Resource<MapSession>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.startSession(id).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun endSession(id: Long): Flow<Resource<MapSession>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.endSession(id).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun joinSession(id: Long): Flow<Resource<MapSession>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.joinSession(id).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun leaveSession(id: Long): Flow<Resource<MapSession>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.leaveSession(id).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun sendMessage(
        id: Long,
        message: NewSessionChatMessageDto
    ): Flow<Resource<List<SessionChatMessage>>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.sendMessage(id, message).map { it.toModel() }))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun markTask(
        id: Long,
        taskId: Long,
        state: Boolean
    ): Flow<Resource<MapSession>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.markTask(id, taskId, state).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }
}
