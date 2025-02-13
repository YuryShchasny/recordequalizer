package com.sb.recordequalizer.root.component

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.sb.core.base.BaseStore
import com.sb.core.resources.theme.AppLanguage
import com.sb.core.resources.theme.ColorUiType

class RootStore : BaseStore() {

    private val _state = MutableValue<State>(State.Progress)
    val state: Value<State> = _state

    fun init() {
        launchIO {
            _state.update { State.Ready() }
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
                            state.copy(colorUiType = newTheme)
                        }
                    }
                }
            }
        }
    }

    sealed interface Intent {
        data object ChangeLanguage : Intent
        data object ChangeTheme : Intent
    }

    sealed interface State {
        data object Progress : State
        data class Ready(
            val language: AppLanguage = AppLanguage.RU,
            val colorUiType: ColorUiType = ColorUiType.DARK
        ) : State
    }
}
