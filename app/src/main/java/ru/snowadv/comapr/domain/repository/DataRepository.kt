package ru.snowadv.comapr.domain.repository

import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.data.remote.dto.CategoryDto
import ru.snowadv.comapr.data.remote.dto.ClearMapSessionDto
import ru.snowadv.comapr.data.remote.dto.MapSessionDto
import ru.snowadv.comapr.data.remote.dto.NewSessionChatMessageDto
import ru.snowadv.comapr.data.remote.dto.RoadMapDto
import ru.snowadv.comapr.domain.model.Category
import ru.snowadv.comapr.domain.model.MapSession
import ru.snowadv.comapr.domain.model.ResponseInfo
import ru.snowadv.comapr.domain.model.RoadMap
import ru.snowadv.comaprbackend.payload.response.ResponseInfoDto

interface DataRepository {
    suspend fun getCategories(): Flow<Resource<List<Category>>>
    suspend fun getCategory( id: Long): Flow<Resource<Category>>
    fun createCategory(token: String, dto: CategoryDto): Flow<Resource<Category>>

    suspend fun fetchRoadMap(id: Long): Flow<Resource<RoadMap>>
    suspend fun fetchMaps(statusId: Int? = null, categoryId: Long? = null): Flow<Resource<List<RoadMap>>>
    suspend fun voteForRoadMap(token: String, id: Long, like: Boolean): Flow<Resource<ResponseInfo>>
    suspend fun changeVerificationStatus(token: String, id: Long, statusId: Int): Flow<Resource<RoadMap>>
    suspend fun updateRoadMap(token: String, dto: RoadMapDto): Flow<Resource<RoadMap>>
    suspend fun createRoadMap(token: String, dto: RoadMapDto): Flow<Resource<RoadMap>>


    suspend fun fetchSessions(): Flow<Resource<List<MapSession>>>
    suspend fun getSession(token: String = "",id: Long): Flow<Resource<MapSession>>
    suspend fun createSession(token: String, dto: ClearMapSessionDto): Flow<Resource<MapSession>>
    suspend fun updateSession(token: String, dto: ClearMapSessionDto, id: Long): Flow<Resource<MapSession>>
    suspend fun startSession(token: String, id: Long): Flow<Resource<ResponseInfo>>
    suspend fun endSession(token: String, id: Long): Flow<Resource<ResponseInfo>>
    suspend fun joinSession(token: String, id: Long): Flow<Resource<MapSession>>
    suspend fun leaveSession(token: String, id: Long): Flow<Resource<ResponseInfo>>
    suspend fun sendMessage(token: String,  id: Long, message: NewSessionChatMessageDto): Flow<Resource<ResponseInfo>>
    suspend fun markTask(
        id: Long,
        token: String,
        taskId: Long,
        state: Boolean
    ): Flow<Resource<MapSession>>
}