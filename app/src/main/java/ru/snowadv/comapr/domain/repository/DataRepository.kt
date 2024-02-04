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
import ru.snowadv.comapr.data.remote.dto.UserAndSessionsDto
import ru.snowadv.comapr.domain.model.Category
import ru.snowadv.comapr.domain.model.MapSession
import ru.snowadv.comapr.domain.model.ResponseInfo
import ru.snowadv.comapr.domain.model.RoadMap
import ru.snowadv.comapr.domain.model.RoadMapItem
import ru.snowadv.comapr.domain.model.SessionChatMessage
import ru.snowadv.comapr.domain.model.UserAndSessions
import ru.snowadv.comaprbackend.dto.CategorizedRoadMaps
import ru.snowadv.comaprbackend.dto.SimpleRoadMapDto
import ru.snowadv.comaprbackend.payload.response.ResponseInfoDto
import java.time.LocalDateTime
import java.time.ZonedDateTime

interface DataRepository {
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
    fun sendMessage(id: Long, message: NewSessionChatMessageDto): Flow<Resource<List<SessionChatMessage>>>
    fun markTask(
        id: Long,
        taskId: Long,
        state: Boolean
    ): Flow<Resource<MapSession>>

    fun getUserInfo(): Flow<Resource<UserAndSessions>>
}