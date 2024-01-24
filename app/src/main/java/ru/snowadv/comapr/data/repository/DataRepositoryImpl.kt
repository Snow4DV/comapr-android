package ru.snowadv.comapr.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.core.util.safeApiCall
import ru.snowadv.comapr.data.remote.ComaprApi
import ru.snowadv.comapr.data.remote.dto.CategoryDto
import ru.snowadv.comapr.data.remote.dto.ClearMapSessionDto
import ru.snowadv.comapr.data.remote.dto.NewSessionChatMessageDto
import ru.snowadv.comapr.data.remote.dto.RoadMapDto
import ru.snowadv.comapr.domain.model.Category
import ru.snowadv.comapr.domain.model.MapSession
import ru.snowadv.comapr.domain.model.ResponseInfo
import ru.snowadv.comapr.domain.model.RoadMap
import ru.snowadv.comapr.domain.repository.DataRepository
import ru.snowadv.comaprbackend.dto.CategorizedRoadMaps

class DataRepositoryImpl(
    private val api: ComaprApi
) : DataRepository {
    override suspend fun getCategories(): Flow<Resource<List<Category>>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.getCategories().map { it.toModel() }))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override suspend fun getCategory(id: Long): Flow<Resource<Category>> = flow {
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

    override suspend fun fetchRoadMap(id: Long): Flow<Resource<RoadMap>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.fetchRoadMap(id).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override suspend fun fetchMaps(
        statusId: Int?, categoryId: Long?
    ): Flow<Resource<List<CategorizedRoadMaps>>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.fetchMaps(statusId, categoryId).map { it.toModel() }))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override suspend fun voteForRoadMap(
        id: Long, like: Boolean
    ): Flow<Resource<ResponseInfo>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.voteForRoadMap(id, like).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override suspend fun changeVerificationStatus(

        id: Long, statusId: Int
    ): Flow<Resource<RoadMap>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.changeVerificationStatus(id, statusId).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override suspend fun updateRoadMap(dto: RoadMapDto): Flow<Resource<RoadMap>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.updateRoadMap(dto).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override suspend fun createRoadMap(dto: RoadMapDto): Flow<Resource<RoadMap>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.createRoadMap(dto).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override suspend fun fetchSessions(): Flow<Resource<List<MapSession>>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.fetchSessions().map { it.toModel() }))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override suspend fun getSession(id: Long): Flow<Resource<MapSession>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.getSession(id).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override suspend fun createSession(
        dto: ClearMapSessionDto
    ): Flow<Resource<MapSession>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.createSession(dto).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override suspend fun updateSession(
        dto: ClearMapSessionDto, id: Long
    ): Flow<Resource<MapSession>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.updateSession(dto, id).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override suspend fun startSession(id: Long): Flow<Resource<ResponseInfo>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.startSession(id).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override suspend fun endSession(id: Long): Flow<Resource<ResponseInfo>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.endSession(id).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override suspend fun joinSession(id: Long): Flow<Resource<MapSession>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.joinSession(id).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override suspend fun leaveSession(id: Long): Flow<Resource<ResponseInfo>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.leaveSession(id).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override suspend fun sendMessage(
        id: Long, message: NewSessionChatMessageDto
    ): Flow<Resource<ResponseInfo>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.sendMessage(id, message).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override suspend fun markTask(
        id: Long, taskId: Long, state: Boolean
    ): Flow<Resource<MapSession>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.markTask(id, taskId, state).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }
}