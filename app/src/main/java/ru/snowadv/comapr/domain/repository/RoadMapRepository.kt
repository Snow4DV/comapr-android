package ru.snowadv.comapr.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.data.remote.dto.CategoryDto
import ru.snowadv.comapr.data.remote.dto.RoadMapDto
import ru.snowadv.comapr.domain.model.CategorizedRoadMaps
import ru.snowadv.comapr.domain.model.Category
import ru.snowadv.comapr.domain.model.ResponseInfo
import ru.snowadv.comapr.domain.model.RoadMap
import ru.snowadv.comapr.domain.model.RoadMapItem

interface RoadMapRepository {
    fun getCategories(): Flow<Resource<List<Category>>>
    fun getCategory(id: Long): Flow<Resource<Category>>
    fun createCategory(dto: CategoryDto): Flow<Resource<Category>>

    fun fetchRoadMap(id: Long): Flow<Resource<RoadMap>>
    fun fetchMaps(
        statusId: Int? = null,
        categoryId: Long? = null
    ): Flow<Resource<List<CategorizedRoadMaps>>>

    fun fetchMapsNames(
        statusId: Int? = null,
        categoryId: Long? = null
    ): Flow<Resource<List<RoadMapItem>>>

    fun voteForRoadMap(id: Long, like: Boolean): Flow<Resource<ResponseInfo>>
    fun changeVerificationStatus(id: Long, statusId: Int): Flow<Resource<RoadMap>>

    fun updateRoadMap(dto: RoadMapDto): Flow<Resource<RoadMap>>
    fun createRoadMap(dto: RoadMapDto): Flow<Resource<RoadMap>>
}
