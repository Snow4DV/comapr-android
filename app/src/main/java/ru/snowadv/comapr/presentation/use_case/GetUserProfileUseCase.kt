package ru.snowadv.comapr.presentation.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.data.remote.dto.UserAndSessionsDto
import ru.snowadv.comapr.domain.model.AuthUser
import ru.snowadv.comapr.domain.model.UserAndSessions
import ru.snowadv.comapr.domain.repository.DataRepository
import ru.snowadv.comapr.domain.repository.SessionRepository
class GetUserProfileUseCase(
    private val dataRepository: DataRepository
) {
    operator fun invoke(): Flow<Resource<UserAndSessions>> {
        return dataRepository.getUserInfo()
    }
}