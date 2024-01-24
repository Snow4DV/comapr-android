package ru.snowadv.comapr.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.snowadv.comapr.data.local.UserDataDao
import ru.snowadv.comapr.data.local.UserDataDb
import ru.snowadv.comapr.data.local.converter.JsonConverter
import ru.snowadv.comapr.data.local.converter.JsonConverterImpl
import ru.snowadv.comapr.data.local.converter.RoomTypeConverter
import ru.snowadv.comapr.data.remote.ComaprApi
import ru.snowadv.comapr.data.repository.DataRepositoryImpl
import ru.snowadv.comapr.data.repository.SessionRepositoryImpl
import ru.snowadv.comapr.domain.repository.DataRepository
import ru.snowadv.comapr.domain.repository.SessionRepository
import ru.snowadv.comapr.presentation.use_case.AuthenticateUseCase
import ru.snowadv.comapr.presentation.use_case.SignInUseCase
import ru.snowadv.comapr.presentation.use_case.SignUpUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson{
        return Gson()
    }

    @Provides
    @Singleton
    fun provideJsonConverter(gson: Gson): JsonConverter {
        return JsonConverterImpl(gson)
    }

    @Provides
    @Singleton
    fun provideRoomTypeConverter(jsonConverter: JsonConverter): RoomTypeConverter {
        return RoomTypeConverter(jsonConverter)
    }

    @Provides
    @Singleton
    fun provideDataRepo(api: ComaprApi): DataRepository {
        return DataRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideDao(db: UserDataDb): UserDataDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(app: Application, typeConverter: RoomTypeConverter): UserDataDb {
        return Room.databaseBuilder(app, UserDataDb::class.java, "user_database")
            .addTypeConverter(typeConverter)
            .build()
    }

    @Provides
    @Singleton
    fun provideSessionRepo(dao: UserDataDao, api: ComaprApi): SessionRepository {
        return SessionRepositoryImpl(dao, api)
    }

    @Provides
    @Singleton
    fun provideApi(): ComaprApi {
        return Retrofit.Builder().baseUrl(ComaprApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ComaprApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthUseCase(sessionRepo: SessionRepository): AuthenticateUseCase {
        return AuthenticateUseCase(sessionRepo)
    }

    @Provides
    @Singleton
    fun provideSignInUseCase(sessionRepo: SessionRepository): SignInUseCase {
        return SignInUseCase(sessionRepo)
    }

    @Provides
    @Singleton
    fun provideSignUpUseCase(sessionRepo: SessionRepository): SignUpUseCase {
        return SignUpUseCase(sessionRepo)
    }
}