package ru.snowadv.comapr.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.snowadv.comapr.data.local.converter.RoomTypeConverter
import ru.snowadv.comapr.data.local.entity.UserData

@Database(
    entities = [UserData::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomTypeConverter::class)
abstract class UserDataDb: RoomDatabase() {
    abstract val dao: UserDataDao
}