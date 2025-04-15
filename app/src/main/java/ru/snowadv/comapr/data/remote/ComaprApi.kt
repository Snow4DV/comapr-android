package ru.snowadv.comapr.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.snowadv.comapr.data.remote.dto.CategoryDto
import ru.snowadv.comapr.data.remote.dto.ClearMapSessionDto
import ru.snowadv.comapr.data.remote.dto.LoginInfoDto
import ru.snowadv.comapr.data.remote.dto.RoadMapDto
import ru.snowadv.comapr.data.remote.dto.SignupInfoDto
import ru.snowadv.comapr.data.remote.dto.JwtAuthDto
import ru.snowadv.comapr.data.remote.dto.MapSessionDto
import ru.snowadv.comapr.data.remote.dto.NewSessionChatMessageDto
import ru.snowadv.comapr.data.remote.dto.SessionChatMessageDto
import ru.snowadv.comapr.data.remote.dto.UserAndSessionsDto
import ru.snowadv.comapr.data.remote.dto.CategorizedRoadMapsDto
import ru.snowadv.comaprbackend.dto.SimpleRoadMapDto
import ru.snowadv.comaprbackend.payload.response.ResponseInfoDto

interface ComaprApi {
    @POST("/api/v1/auth/signin")
    suspend fun loginUser(@Body loginRequest: LoginInfoDto): JwtAuthDto
    @POST("/api/v1/auth/signup")
    suspend fun registerUser(@Body signUpRequest: SignupInfoDto): JwtAuthDto
    @POST("/api/v1/auth/authenticate")
    suspend fun authUser(@Header("Authorization") token: String): JwtAuthDto
    @GET("/api/v1/category/list")
    suspend fun getCategories(): List<CategoryDto>
    // и другие ручки

    @GET("/api/v1/category/{id}")
    suspend fun getCategory(@Path("id") id: Long): CategoryDto

    @POST("/api/v1/category/create")
    fun createCategory(@Body dto: CategoryDto): CategoryDto

    @GET("/api/v1/roadmap")
    suspend fun fetchRoadMap(@Query("id") id: Long): RoadMapDto

    @GET("/api/v1/roadmap/list")
    suspend fun fetchMaps(@Query("statusId") statusId: Int?, @Query("categoryId") categoryId: Long?): List<CategorizedRoadMapsDto>

    @GET("/api/v1/roadmap/namesList")
    suspend fun fetchMapsNames(@Query("statusId") statusId: Int?, @Query("categoryId") categoryId: Long?): List<SimpleRoadMapDto>

    @PUT("/api/v1/roadmap/vote")
    suspend fun voteForRoadMap(@Query("id") id: Long, @Query("like") like: Boolean): ResponseInfoDto

    @PUT("/api/v1/roadmap/changeStatus")
    suspend fun changeVerificationStatus(@Query("id") id: Long, @Query("statusId") statusId: Int): RoadMapDto

    @PUT("/api/v1/roadmap/update")
    suspend fun updateRoadMap(@Body dto: RoadMapDto): RoadMapDto

    @POST("/api/v1/roadmap/create")
    suspend fun createRoadMap(@Body dto: RoadMapDto): RoadMapDto

    @GET("/api/v1/session/list")
    suspend fun fetchSessions(): List<MapSessionDto>

    @GET("/api/v1/session/{id}")
    suspend fun getSession(@Path("id") id: Long): MapSessionDto

    @POST("/api/v1/session/create")
    suspend fun createSession(@Body dto: ClearMapSessionDto): MapSessionDto

    @POST("/api/v1/session/{id}/update")
    suspend fun updateSession(@Body dto: ClearMapSessionDto, @Path("id") id: Long): MapSessionDto

    @POST("/api/v1/session/{id}/start")
    suspend fun startSession(@Path("id") id: Long): MapSessionDto

    @POST("/api/v1/session/{id}/end")
    suspend fun endSession(@Path("id") id: Long): MapSessionDto

    @POST("/api/v1/session/{id}/join")
    suspend fun joinSession(@Path("id") id: Long): MapSessionDto

    @POST("/api/v1/session/{id}/leave")
    suspend fun leaveSession(@Path("id") id: Long): MapSessionDto

    @POST("/api/v1/session/{id}/sendMessage")
    suspend fun sendMessage(@Path("id") id: Long, @Body message: NewSessionChatMessageDto): List<SessionChatMessageDto>

    @POST("/api/v1/session/{id}/markTask/{taskId}")
    suspend fun markTask(
        @Path("id") id: Long,
        @Path("taskId") taskId: Long,
        @Query("state") state: Boolean
    ): MapSessionDto


    @GET("/api/v1/user/info")
    suspend fun getUserInfo(): UserAndSessionsDto

    @GET("/api/v1/user/activeSessions")
    suspend fun getActiveSessions(): List<MapSessionDto>

    companion object {
        const val BASE_URL = "http://185.154.194.125:8086/"
    }
}