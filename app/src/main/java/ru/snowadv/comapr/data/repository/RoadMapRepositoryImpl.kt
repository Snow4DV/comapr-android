package ru.snowadv.comapr.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.core.util.safeApiCall
import ru.snowadv.comapr.data.remote.ComaprApi
import ru.snowadv.comapr.data.remote.dto.CategoryDto
import ru.snowadv.comapr.data.remote.dto.RoadMapDto
import ru.snowadv.comapr.domain.model.Category
import ru.snowadv.comapr.domain.model.CategorizedRoadMaps
import ru.snowadv.comapr.domain.model.ResponseInfo
import ru.snowadv.comapr.domain.model.RoadMap
import ru.snowadv.comapr.domain.model.RoadMapItem
import ru.snowadv.comapr.domain.repository.RoadMapRepository

class RoadMapRepositoryImpl(
    private val api: ComaprApi
) : RoadMapRepository {

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
        statusId: Int?,
        categoryId: Long?
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
        id: Long,
        like: Boolean
    ): Flow<Resource<ResponseInfo>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.voteForRoadMap(id, like).toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }

    override fun changeVerificationStatus(
        id: Long,
        statusId: Int
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
}
