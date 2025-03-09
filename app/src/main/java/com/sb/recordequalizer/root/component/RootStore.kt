package com.sb.recordequalizer.root.component

import android.util.Log
import androidx.compose.runtime.Immutable
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.sb.audio_processor.AudioEngine
import com.sb.core.base.BaseStore
import com.sb.core.resources.theme.AppLanguage
import com.sb.core.resources.theme.ColorUiType
import com.sb.data.local.DefaultFrequencies
import com.sb.domain.entity.Profile
import com.sb.domain.entity.Settings
import com.sb.domain.repository.ProfilesRepository
import com.sb.domain.repository.SettingsRepository
import org.koin.core.component.inject

class RootStore(override val lifecycle: Lifecycle) : BaseStore(), LifecycleOwner {

    private val profilesRepository by inject<ProfilesRepository>()
    private val settingsRepository by inject<SettingsRepository>()
    private val audioEngine by inject<AudioEngine>()

    private val _state = MutableValue<State>(State.Progress)
    val state: Value<State> = _state

    init {
        launchMain {
            Log.d("MY_TAG", "CREATE")
            audioEngine.onCreate()
        }
        lifecycle.doOnDestroy {
            launchIO {
                audioEngine.onDestroy()
            }
        }
        launchIO {
            val defaultProfile = Profile(
                name = "Default",
                gains = DefaultFrequencies.get().map { it.second },
                amplitude = 0f,
                leftChannel = false,
                rightChannel = true,
            )
            val profile = if (profilesRepository.getProfiles().isEmpty()) {
                profilesRepository.addProfile(defaultProfile)
                profilesRepository.setSelectedProfile(profilesRepository.getProfiles().first().id)
                defaultProfile
            } else {
                profilesRepository.getSelectedProfile() ?: defaultProfile
            }
            audioEngine.initEqualizer(
                profile = profile,
                frequencies = DefaultFrequencies.get().map { it.first },
            )
        }

    }

    fun dispatchIntent(intent: Intent) {
        when (intent) {
            Intent.ChangeLanguage -> {
                _state.update { state ->
                    when (state) {
                        State.Progress -> state
                        is State.Ready -> {
                            val currentLanguageIndex =
                                AppLanguage.entries.indexOf(state.language)
                            val newLanguageIndex =
                                (currentLanguageIndex + 1) % AppLanguage.entries.size
                            val newLanguage = AppLanguage.entries[newLanguageIndex]
                            state.copy(language = newLanguage)
                        }
                    }
                }
            }

            Intent.ChangeTheme -> {
                _state.update { state ->
                    when (state) {
                        State.Progress -> state
                        is State.Ready -> {
                            val newTheme =
                                if (state.colorUiType == ColorUiType.DARK) ColorUiType.LIGHT else ColorUiType.DARK
                            launchIO { saveTheme(newTheme) }
                            state.copy(colorUiType = newTheme)
                        }
                    }
                }
            }

            Intent.PermissionsGranted -> {
                launchIO {
                    val colorUiType = getColorUiType()
                    _state.update { State.Ready(hasPermissions = true, colorUiType = colorUiType) }
                }
            }

            Intent.PermissionsDenied -> {
                launchIO {
                    val colorUiType = getColorUiType()
                    _state.update { State.Ready(hasPermissions = false, colorUiType = colorUiType) }
                }
            }
        }
    }

    private suspend fun getColorUiType(): ColorUiType {
        val settings = settingsRepository.getSettings()
        return when (settings.theme) {
            Settings.Theme.DARK -> ColorUiType.DARK
            Settings.Theme.LIGHT -> ColorUiType.LIGHT
        }
    }

    private suspend fun saveTheme(colorUiType: ColorUiType) {
        val settings = settingsRepository.getSettings()
        settingsRepository.saveSettings(
            settings.copy(
                theme = when (colorUiType) {
                    ColorUiType.DARK -> Settings.Theme.DARK
                    ColorUiType.LIGHT -> Settings.Theme.LIGHT
                }
            )
        )
    }

    @Immutable
    sealed interface State {
        data object Progress : State
        data class Ready(
            val hasPermissions: Boolean = false,
            val language: AppLanguage = AppLanguage.RU,
            val colorUiType: ColorUiType = ColorUiType.DARK
        ) : State
    }

    sealed interface Intent {
        data object ChangeLanguage : Intent
        data object ChangeTheme : Intent
        data object PermissionsGranted : Intent
        data object PermissionsDenied : Intent
    }
}
