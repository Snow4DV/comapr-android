package ru.snowadv.comapr.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.core.util.safeApiCall
import ru.snowadv.comapr.core.util.toUtcLocalDateTime
import ru.snowadv.comapr.data.remote.ComaprApi
import ru.snowadv.comapr.data.remote.dto.CategoryDto
import ru.snowadv.comapr.data.remote.dto.ClearMapSessionDto
import ru.snowadv.comapr.data.remote.dto.NewSessionChatMessageDto
import ru.snowadv.comapr.data.remote.dto.RoadMapDto
import ru.snowadv.comapr.domain.model.Category
import ru.snowadv.comapr.domain.model.MapSession
import ru.snowadv.comapr.domain.model.ResponseInfo
import ru.snowadv.comapr.domain.model.RoadMap
import ru.snowadv.comapr.domain.model.RoadMapItem
import ru.snowadv.comapr.domain.model.SessionChatMessage
import ru.snowadv.comapr.domain.model.UserAndSessions
import ru.snowadv.comapr.domain.repository.DataRepository
import ru.snowadv.comaprbackend.dto.CategorizedRoadMaps
import ru.snowadv.comaprbackend.dto.SimpleRoadMapDto
import java.time.LocalDateTime
import java.time.ZonedDateTime

class DataRepositoryImpl(
    private val api: ComaprApi
) : DataRepository {
    override fun getCategories(): Flow<Resource<List<Category>>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.getCategories().map { it.toModel() }))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun getCategory(id: Long): Flow<Resource<Category>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.getCategory(id).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun createCategory(dto: CategoryDto): Flow<Resource<Category>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.createCategory(dto).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun fetchRoadMap(id: Long): Flow<Resource<RoadMap>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.fetchRoadMap(id).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun fetchMaps(
        statusId: Int?, categoryId: Long?
    ): Flow<Resource<List<CategorizedRoadMaps>>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.fetchMaps(statusId, categoryId).map { it.toModel() }))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun fetchMapsNames(
        statusId: Int?,
        categoryId: Long?
    ): Flow<Resource<List<RoadMapItem>>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.fetchMapsNames(statusId, categoryId).map { it.toModel() }))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun voteForRoadMap(
        id: Long, like: Boolean
    ): Flow<Resource<ResponseInfo>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.voteForRoadMap(id, like).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun changeVerificationStatus(

        id: Long, statusId: Int
    ): Flow<Resource<RoadMap>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.changeVerificationStatus(id, statusId).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun updateRoadMap(dto: RoadMapDto): Flow<Resource<RoadMap>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.updateRoadMap(dto).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun createRoadMap(dto: RoadMapDto): Flow<Resource<RoadMap>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.createRoadMap(dto).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun fetchSessions(): Flow<Resource<List<MapSession>>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.fetchSessions().map { it.toModel() }))
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
        val dto = ClearMapSessionDto(public, startDate.toUtcLocalDateTime(), groupChatUrl, roadMapId)
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
        val dto = ClearMapSessionDto(public, startDate.toUtcLocalDateTime(), groupChatUrl, roadMapId)
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
        id: Long, message: NewSessionChatMessageDto
    ): Flow<Resource<List<SessionChatMessage>>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.sendMessage(id, message).map { it.toModel() }))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun markTask(
        id: Long, taskId: Long, state: Boolean
    ): Flow<Resource<MapSession>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.markTask(id, taskId, state).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun getUserInfo(): Flow<Resource<UserAndSessions>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.getUserInfo().toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }
}