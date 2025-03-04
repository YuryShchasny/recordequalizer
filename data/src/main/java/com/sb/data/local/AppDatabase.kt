package com.sb.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sb.data.local.converter.FloatListConverter
import com.sb.data.local.dao.ProfilesDao
import com.sb.data.local.dbo.ProfileDbo

@Database(
    entities = [ProfileDbo::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(FloatListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profilesDao(): ProfilesDao
}
