package ru.snowadv.comapr.domain.repository


import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.domain.model.AuthUser

interface AuthRepository {
    suspend fun signIn(username: String, password: String): Resource<AuthUser>
    suspend fun signUp(email: String, username: String, password: String): Resource<AuthUser>
    suspend fun authenticate(): Resource<AuthUser>
}