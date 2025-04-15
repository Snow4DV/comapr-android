package ru.snowadv.comapr.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.core.util.safeApiCall
import ru.snowadv.comapr.data.remote.ComaprApi
import ru.snowadv.comapr.domain.model.UserAndSessions
import ru.snowadv.comapr.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val api: ComaprApi
) : ProfileRepository {

    override fun getUserInfo(): Flow<Resource<UserAndSessions>> = flow {
        emit(Resource.Loading())
        safeApiCall(block = {
            emit(Resource.Success(api.getUserInfo().toModel()))
        }, onException = {
            emit(Resource.Error(it))
        })
    }
}
