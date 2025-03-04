package com.sb.data.repository

import com.sb.data.local.datasource.SettingsDataSource
import com.sb.domain.entity.Settings
import com.sb.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val settingsDataSource: SettingsDataSource
) : SettingsRepository {
    override suspend fun getSettings(): Settings = settingsDataSource.getSettings()

    override suspend fun saveSettings(settings: Settings) =
        settingsDataSource.saveSettings(settings)
}
