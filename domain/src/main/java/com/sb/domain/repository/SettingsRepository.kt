package com.sb.domain.repository

import com.sb.domain.entity.Settings

interface SettingsRepository {
    suspend fun getSettings(): Settings
    suspend fun saveSettings(settings: Settings)
}