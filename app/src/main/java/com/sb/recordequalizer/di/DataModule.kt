package com.sb.recordequalizer.di

import androidx.room.Room
import com.sb.data.local.AppDatabase
import com.sb.data.local.dao.ProfilesDao
import com.sb.data.local.datasource.ProfilesDataSource
import com.sb.data.local.datasource.SettingsDataSource
import com.sb.data.repository.ProfilesRepositoryImpl
import com.sb.data.repository.SettingsRepositoryImpl
import com.sb.domain.repository.ProfilesRepository
import com.sb.domain.repository.SettingsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single<AppDatabase> {
        Room
            .databaseBuilder(
                get(),
                AppDatabase::class.java,
                "record_equalizer.db",
            ).build()
    }
    single<ProfilesDao> {
        val database: AppDatabase = get()
        database.profilesDao()
    }
    singleOf(::ProfilesRepositoryImpl).bind<ProfilesRepository>()
    singleOf(::SettingsRepositoryImpl).bind<SettingsRepository>()
    singleOf(::ProfilesDataSource)
    singleOf(::SettingsDataSource)
}
