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

class DataRepositoryImpl(
    private val api: ComaprApi
): DataRepository {
    override suspend fun getCategories(): Flow<Resource<List<Category>>> = flow {
        emit(Resource.Loading())
        safeApiCall(
            block = {
                emit(Resource.Success(api.getCategories().map { it.toModel() }))
            },
            onException = {
                emit(Resource.Error(it))
            }
        )
    }

    override suspend fun getCategory(id: Long): Flow<Resource<Category>> = flow {
        emit(Resource.Loading())
        safeApiCall(
            block = {
                emit(Resource.Success(api.getCategory(id).toModel()))
            },
            onException = {
                emit(Resource.Error(it))
            }
        )
    }

    override fun createCategory(token: String, dto: CategoryDto): Flow<Resource<Category>> = flow {
        emit(Resource.Loading())
        safeApiCall(
            block = {
                emit(Resource.Success(api.createCategory(token, dto).toModel()))
            },
            onException = {
                emit(Resource.Error(it))
            }
        )
    }

    override suspend fun fetchRoadMap(id: Long): Flow<Resource<RoadMap>> = flow {
        emit(Resource.Loading())
        safeApiCall(
            block = {
                emit(Resource.Success(api.fetchRoadMap(id).toModel()))
            },
            onException = {
                emit(Resource.Error(it))
            }
        )
    }

    override suspend fun fetchMaps(
        statusId: Int?,
        categoryId: Long?
    ): Flow<Resource<List<RoadMap>>> = flow {
        emit(Resource.Loading())
        safeApiCall(
            block = {
                emit(Resource.Success(api.fetchMaps(statusId, categoryId).map { it.toModel() }))
            },
            onException = {
                emit(Resource.Error(it))
            }
        )
    }

    override suspend fun voteForRoadMap(
        token: String,
        id: Long,
        like: Boolean
    ): Flow<Resource<ResponseInfo>> = flow {
        emit(Resource.Loading())
        safeApiCall(
            block = {
                emit(Resource.Success(api.voteForRoadMap(token, id, like).toModel()))
            },
            onException = {
                emit(Resource.Error(it))
            }
        )
    }

    override suspend fun changeVerificationStatus(
        token: String,
        id: Long,
        statusId: Int
    ): Flow<Resource<RoadMap>> = flow {
        emit(Resource.Loading())
        safeApiCall(
            block = {
                emit(Resource.Success(api.changeVerificationStatus(token, id, statusId).toModel()))
            },
            onException = {
                emit(Resource.Error(it))
            }
        )
    }

    override suspend fun updateRoadMap(token: String, dto: RoadMapDto): Flow<Resource<RoadMap>> = flow {
        emit(Resource.Loading())
        safeApiCall(
            block = {
                emit(Resource.Success(api.updateRoadMap(token, dto).toModel()))
            },
            onException = {
                emit(Resource.Error(it))
            }
        )
    }

    override suspend fun createRoadMap(token: String, dto: RoadMapDto): Flow<Resource<RoadMap>> = flow {
        emit(Resource.Loading())
        safeApiCall(
            block = {
                emit(Resource.Success(api.createRoadMap(token, dto).toModel()))
            },
            onException = {
                emit(Resource.Error(it))
            }
        )
    }
    override suspend fun fetchSessions(): Flow<Resource<List<MapSession>>> = flow {
        emit(Resource.Loading())
        safeApiCall(
            block = {
                emit(Resource.Success(api.fetchSessions().map { it.toModel() }))
            },
            onException = {
                emit(Resource.Error(it))
            }
        )
    }

    override suspend fun getSession(token: String, id: Long): Flow<Resource<MapSession>> = flow {
        emit(Resource.Loading())
        safeApiCall(
            block = {
                emit(Resource.Success(api.getSession(token, id).toModel()))
            },
            onException = {
                emit(Resource.Error(it))
            }
        )
    }

    override suspend fun createSession(
        token: String,
        dto: ClearMapSessionDto
    ): Flow<Resource<MapSession>> = flow {
        emit(Resource.Loading())
        safeApiCall(
            block = {
                emit(Resource.Success(api.createSession(token, dto).toModel()))
            },
            onException = {
                emit(Resource.Error(it))
            }
        )
    }

    override suspend fun updateSession(
        token: String,
        dto: ClearMapSessionDto,
        id: Long
    ): Flow<Resource<MapSession>> = flow {
        emit(Resource.Loading())
        safeApiCall(
            block = {
                emit(Resource.Success(api.updateSession(token, dto, id).toModel()))
            },
            onException = {
                emit(Resource.Error(it))
            }
        )
    }

    override suspend fun startSession(token: String, id: Long): Flow<Resource<ResponseInfo>> = flow {
        emit(Resource.Loading())
        safeApiCall(
            block = {
                emit(Resource.Success(api.startSession(token, id).toModel()))
            },
            onException = {
                emit(Resource.Error(it))
            }
        )
    }

    override suspend fun endSession(token: String, id: Long): Flow<Resource<ResponseInfo>> = flow {
        emit(Resource.Loading())
        safeApiCall(
            block = {
                emit(Resource.Success(api.endSession(token, id).toModel()))
            },
            onException = {
                emit(Resource.Error(it))
            }
        )
    }

    override suspend fun joinSession(token: String, id: Long): Flow<Resource<MapSession>> = flow {
        emit(Resource.Loading())
        safeApiCall(
            block = {
                emit(Resource.Success(api.joinSession(token, id).toModel()))
            },
            onException = {
                emit(Resource.Error(it))
            }
        )
    }

    override suspend fun leaveSession(token: String, id: Long): Flow<Resource<ResponseInfo>> = flow {
        emit(Resource.Loading())
        safeApiCall(
            block = {
                emit(Resource.Success(api.leaveSession(token, id).toModel()))
            },
            onException = {
                emit(Resource.Error(it))
            }
        )
    }

    override suspend fun sendMessage(
        token: String,
        id: Long,
        message: NewSessionChatMessageDto
    ): Flow<Resource<ResponseInfo>> = flow {
        emit(Resource.Loading())
        safeApiCall(
            block = {
                emit(Resource.Success(api.sendMessage(token, id, message).toModel()))
            },
            onException = {
                emit(Resource.Error(it))
            }
        )
    }

    override suspend fun markTask(
        id: Long,
        token: String,
        taskId: Long,
        state: Boolean
    ): Flow<Resource<MapSession>> = flow {
        emit(Resource.Loading())
        safeApiCall(
            block = {
                emit(Resource.Success(api.markTask(id, taskId, state).toModel()))
            },
            onException = {
                emit(Resource.Error(it))
            }
        )
    }
}