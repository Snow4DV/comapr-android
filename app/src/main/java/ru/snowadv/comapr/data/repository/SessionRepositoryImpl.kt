package ru.snowadv.comapr.data.repository

import retrofit2.HttpException
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.data.local.UserDataDao
import ru.snowadv.comapr.data.local.entity.UserData
import ru.snowadv.comapr.data.remote.ComaprApi
import ru.snowadv.comapr.data.remote.dto.LoginInfoDto
import ru.snowadv.comapr.data.remote.dto.SignupInfoDto
import ru.snowadv.comapr.domain.model.AuthUser
import ru.snowadv.comapr.domain.repository.SessionRepository

class SessionRepositoryImpl(
    private val dao: UserDataDao,
    private val api: ComaprApi
) : SessionRepository {
    override suspend fun signIn(username: String, password: String): Resource<AuthUser> {
        return getJwtToken(LoginInfoDto(username, password))
    }

    override suspend fun signUp(
        email: String,
        username: String,
        password: String
    ): Resource<AuthUser> {
        return getJwtToken(SignupInfoDto(username, email, password))
    }

    private suspend fun getJwtToken(requestOrUserData: Any): Resource<AuthUser> {
        try {
            val jwtAuthDto = when(requestOrUserData) {
                is LoginInfoDto -> api.loginUser(requestOrUserData)
                is SignupInfoDto -> api.registerUser(requestOrUserData)
                is UserData -> api.authUser("Bearer ${requestOrUserData.token}") // token/roles might get updated by server
                else -> return Resource.Error("Unknown error")
            }
            val newUserData = UserData(
                1,
                jwtAuthDto.accessToken,
                jwtAuthDto.username,
                jwtAuthDto.email,
                jwtAuthDto.roles
            )
            dao.insertNewUserData(
                newUserData
            )
            return Resource.Success(newUserData.toModel())
        } catch (e: HttpException) {
            return if (e.code() == 401) {
                dao.removeUserData()
                Resource.Error("Wrong login or password")
            } else {
                Resource.Error(e.response().toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error("No connection")
        }
    }

    override suspend fun authenticate(): Resource<AuthUser> {
        val dbUserData = dao.getUserData() ?: return Resource.Error("Not authorized")
        return getJwtToken(dbUserData)
    }

}