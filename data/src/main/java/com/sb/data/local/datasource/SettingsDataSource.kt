package com.sb.data.local.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sb.domain.entity.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private const val SETTINGS_PREFERENCES = "com.sb.recordequalizer.preferences"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_PREFERENCES)

class SettingsDataSource(
    context: Context
) {
    private val dataStore: DataStore<Preferences> =
        context.dataStore

    suspend fun saveSettings(value: Settings) {
        withContext(Dispatchers.IO) {
            dataStore.edit { prefs ->
                prefs[SETTINGS_SELECTED_PROFILE] = value.selectedProfileId
                prefs[SETTINGS_SELECTED_INPUT_DEVICE] = value.selectedInputDevice
                prefs[SETTINGS_SELECTED_OUTPUT_DEVICE] = value.selectedOutputDevice
                prefs[SETTINGS_THEME] = value.theme.name
            }
        }
    }

    suspend fun getSettings(): Settings = withContext(Dispatchers.IO) {
        val flow = dataStore.data
            .map { prefs ->
                Settings(
                    selectedProfileId = prefs[SETTINGS_SELECTED_PROFILE] ?: -1,
                    selectedInputDevice = prefs[SETTINGS_SELECTED_INPUT_DEVICE] ?: -1,
                    selectedOutputDevice = prefs[SETTINGS_SELECTED_OUTPUT_DEVICE] ?: -1,
                    theme = Settings.Theme.valueOf(
                        prefs[SETTINGS_THEME] ?: Settings.Theme.DARK.name
                    )
                )
            }
        flow.firstOrNull() ?: Settings()
    }

    suspend fun setSelectedProfile(id: Long) {
        withContext(Dispatchers.IO) {
            dataStore.edit { prefs ->
                prefs[SETTINGS_SELECTED_PROFILE] = id
            }
        }
    }

    companion object {
        val SETTINGS_SELECTED_PROFILE = longPreferencesKey("settings_selected_profile")
        val SETTINGS_SELECTED_INPUT_DEVICE = intPreferencesKey("settings_selected_input_device")
        val SETTINGS_SELECTED_OUTPUT_DEVICE = intPreferencesKey("settings_selected_output_device")
        val SETTINGS_THEME = stringPreferencesKey("settings_theme")
    }
}