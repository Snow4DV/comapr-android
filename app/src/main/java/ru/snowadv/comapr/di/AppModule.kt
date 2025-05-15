package ru.snowadv.comapr.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.snowadv.comapr.data.converter.JsonConverter
import ru.snowadv.comapr.data.converter.JsonConverterImpl
import ru.snowadv.comapr.data.converter.RoomTypeConverter
import ru.snowadv.comapr.data.local.UserDataDao
import ru.snowadv.comapr.data.local.UserDataDb
import ru.snowadv.comapr.data.remote.ApiAuthenticator
import ru.snowadv.comapr.data.remote.ComaprApi
import ru.snowadv.comapr.data.repository.DataRepositoryImpl
import ru.snowadv.comapr.data.repository.AuthRepositoryImpl
import ru.snowadv.comapr.domain.repository.DataRepository
import ru.snowadv.comapr.domain.repository.AuthRepository
import ru.snowadv.comapr.presentation.EventAggregator
import ru.snowadv.comapr.presentation.EventAggregatorImpl
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return GsonBuilder()

            .registerTypeAdapter(
            LocalDateTime::class.java, JsonDeserializer { json, type, jsonDeserializationContext ->
                LocalDateTime.parse(json.asJsonPrimitive.asString)
            })
            .registerTypeAdapter(
                LocalDateTime::class.java, JsonSerializer<LocalDateTime> {localDateTime, type, jsonSerializationContext ->
                    JsonPrimitive(localDateTime.format(formatter))
                }
            )
            .create()
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
    fun provideDao(db: UserDataDb): UserDataDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun provideAuthRepo(dao: UserDataDao, api: ComaprApi): AuthRepository {
        return AuthRepositoryImpl(dao, api)
    }

    @Provides
    @Singleton
    fun provideDataRepo(api: ComaprApi): DataRepository {
        return DataRepositoryImpl(api)
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
    fun provideApiAuthenticator(): ApiAuthenticator {
        return ApiAuthenticator()
    }

    @Provides
    @Singleton
    fun provideInterceptor(apiAuthenticator: ApiAuthenticator): Interceptor {
        return apiAuthenticator
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor)
            .addInterceptor(HttpLoggingInterceptor())
            .connectTimeout(15, TimeUnit.SECONDS).build()
    }


    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideApi(okHttpClient: OkHttpClient, factory: GsonConverterFactory): ComaprApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(ComaprApi.BASE_URL)
            .addConverterFactory(factory)
            .build()
            .create(ComaprApi::class.java)
    }

    @Provides
    @Singleton
    fun provideEventAggregator(): EventAggregator {
        return EventAggregatorImpl()
    }

}