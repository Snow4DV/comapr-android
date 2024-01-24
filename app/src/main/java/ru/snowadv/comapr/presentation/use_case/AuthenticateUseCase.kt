package ru.snowadv.comapr.presentation.use_case

import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.data.local.entity.UserData
import ru.snowadv.comapr.domain.model.AuthUser
import ru.snowadv.comapr.domain.repository.SessionRepository

class AuthenticateUseCase(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(): Resource<AuthUser> {
        return sessionRepository.authenticate()
    }
}