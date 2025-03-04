package com.sb.home.presentation.component

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import androidx.compose.runtime.Immutable
import com.sb.audio_processor.AudioEngine
import com.sb.core.base.BaseStore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.component.inject


class HomeStore : BaseStore() {

    private val context by inject<Context>()

    private val audioEngine by inject<AudioEngine>()

    private var _uiState = MutableStateFlow<State?>(null)
    val state = _uiState.asStateFlow()

    private var _error = MutableSharedFlow<Error>()
    val error = _error.asSharedFlow()

    init {
        val mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val inputDevices = mAudioManager.getDevices(AudioManager.GET_DEVICES_INPUTS)
            .filter { it.type == AudioDeviceInfo.TYPE_BUILTIN_MIC && it.address != "back" }
        val outputDevices = mAudioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
            .filter { it.type != AudioDeviceInfo.TYPE_TELEPHONY }
        launchIO {
            _uiState.update {
                State(
                    inputDevices = inputDevices.toList(),
                    outputDevices = outputDevices.toList()
                )
            }
        }
    }

    fun dispatchIntent(intent: Intent) {
        when (intent) {
            Intent.PlayPause -> {
                launchIO {
                    val result =
                        if (audioEngine.audioIsPlaying()) audioEngine.pauseAudio() else audioEngine.playAudio()
                    _uiState.update { it?.copy(playing = result) }
                }
            }

            is Intent.SelectInputDevice -> {
                launchIO {
                    try {
                        audioEngine.setInputDevice(intent.deviceInfo.id)
                        _uiState.update {
                            it?.copy(
                                selectedInputDevice = intent.deviceInfo
                            )
                        }
                    } catch (e: Throwable) {
                        _error.emit(Error.SelectDeviceError)
                        _uiState.update {
                            it?.copy(
                                playing = false
                            )
                        }
                        e.printStackTrace()
                    }
                }
            }

            is Intent.SelectOutputDevice -> {
                launchIO {
                    try {
                        audioEngine.setOutputDevice(intent.deviceInfo.id)
                        _uiState.update {
                            it?.copy(
                                selectedOutputDevice = intent.deviceInfo
                            )
                        }
                    } catch (e: Throwable) {
                        _error.emit(Error.SelectDeviceError)
                        _uiState.update {
                            it?.copy(
                                playing = false
                            )
                        }
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    @Immutable
    data class State(
        val playing: Boolean = false,
        val selectedInputDevice: AudioDeviceInfo? = null,
        val selectedOutputDevice: AudioDeviceInfo? = null,
        val inputDevices: List<AudioDeviceInfo> = emptyList(),
        val outputDevices: List<AudioDeviceInfo> = emptyList(),
    )

    sealed interface Intent {
        data object PlayPause : Intent
        data class SelectInputDevice(val deviceInfo: AudioDeviceInfo) : Intent
        data class SelectOutputDevice(val deviceInfo: AudioDeviceInfo) : Intent
    }

    sealed interface Error {
        data object SelectDeviceError : Error
    }
}
