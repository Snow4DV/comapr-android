package ru.snowadv.comapr.domain.repository


import android.provider.ContactsContract.CommonDataKinds.Email
import kotlinx.coroutines.flow.Flow
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.data.local.entity.UserData
import ru.snowadv.comapr.domain.model.AuthUser

interface SessionRepository {
    suspend fun signIn(username: String, password: String): Resource<AuthUser>
    suspend fun signUp(email: String, username: String, password: String): Resource<AuthUser>
    suspend fun authenticate(): Resource<AuthUser>
}