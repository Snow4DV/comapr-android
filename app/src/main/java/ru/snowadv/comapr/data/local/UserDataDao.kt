package ru.snowadv.comapr.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.snowadv.comapr.data.local.entity.UserData

@Dao
interface UserDataDao {
    @Query("SELECT * FROM UserData limit 1")
    suspend fun getUserData(): UserData?
    @Query("DELETE FROM UserData")
    suspend fun removeUserData()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewUserData(data: UserData)
}