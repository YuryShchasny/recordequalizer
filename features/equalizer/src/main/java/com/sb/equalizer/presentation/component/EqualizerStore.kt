package com.sb.equalizer.presentation.component

import androidx.compose.runtime.Immutable
import com.sb.audio_processor.AudioEngine
import com.sb.core.base.BaseStore
import com.sb.domain.entity.DefaultFrequencies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.component.inject


class EqualizerStore : BaseStore() {

    private val audioEngine by inject<AudioEngine>()

    private var _uiState = MutableStateFlow<State?>(null)
    val state = _uiState.asStateFlow()

    init {
        launchIO {
            _uiState.update {
                State(
                    amplitude = 0f,
                    frequencies = DefaultFrequencies.get(),
                )
            }
        }
    }

    fun dispatchIntent(intent: Intent) {
        when (intent) {

            is Intent.ChangeLeftChannel -> {
                launchIO {
                    audioEngine.changeLeftChannel(intent.enabled)
                }
            }

            is Intent.ChangeRightChannel -> {
                launchIO {
                    audioEngine.changeRightChannel(intent.enabled)
                }
            }

            is Intent.FrequencyGainChanged -> {
                launchIO {
                    _uiState.update { state ->
                        state?.copy(
                            frequencies = state.frequencies.map { frequency ->
                                if (frequency.first == intent.frequency) {
                                    frequency.copy(second = intent.value)
                                } else frequency
                            }
                        )
                    }
                    audioEngine.setFrequencyGain(intent.frequency, intent.value)
                }
            }

            is Intent.AmplitudeGainChanged -> {
                launchIO {
                    _uiState.update { state ->
                        state?.copy(
                            amplitude = intent.value
                        )
                    }
                    audioEngine.setAmplitudeGain(intent.value)
                }
            }
        }
    }

    @Immutable
    data class State(
        val frequencies: List<Pair<Int, Float>> = listOf(),
        val amplitude: Float = 0f,
    )

    sealed interface Intent {
        data class ChangeLeftChannel(val enabled: Boolean) : Intent
        data class ChangeRightChannel(val enabled: Boolean) : Intent
        data class FrequencyGainChanged(val frequency: Int, val value: Float) : Intent
        data class AmplitudeGainChanged(val value: Float) : Intent
    }
}
