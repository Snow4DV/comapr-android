package ru.snowadv.comapr.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.domain.model.UserAndSessions

interface ProfileRepository {
    fun getUserInfo(): Flow<Resource<UserAndSessions>>
}
