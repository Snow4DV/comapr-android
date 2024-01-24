package ru.snowadv.comapr.presentation.use_case

import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.data.local.entity.UserData
import ru.snowadv.comapr.domain.model.AuthUser
import ru.snowadv.comapr.domain.repository.SessionRepository

class SignInUseCase(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(username: String, password: String): Resource<AuthUser> {
        return sessionRepository.signIn(username, password)
    }
}