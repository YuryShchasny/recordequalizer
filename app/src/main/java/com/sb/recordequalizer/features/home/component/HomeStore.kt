package com.sb.recordequalizer.features.home.component

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import com.sb.audio_processor.AudioEngine
import com.sb.audio_processor.NativeAudioEngine
import com.sb.core.base.BaseStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.component.inject


class HomeStore(override val lifecycle: Lifecycle) : BaseStore(), LifecycleOwner {

    private val context by inject<Context>()

    private val audioEngine: AudioEngine = NativeAudioEngine(context)

    private var _uiState = MutableStateFlow(State())
    val state = _uiState.asStateFlow()

    init {
        val mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val inputDevices = mAudioManager.getDevices(AudioManager.GET_DEVICES_INPUTS)
            .filter { it.type == AudioDeviceInfo.TYPE_BUILTIN_MIC && it.address != "back" }
        val outputDevices = mAudioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
            .filter { it.type != AudioDeviceInfo.TYPE_TELEPHONY }
        launchIO {
            _uiState.update {
                it.copy(
                    inputDevices = inputDevices.toList(),
                    outputDevices = outputDevices.toList()
                )
            }
        }
        lifecycle.doOnStop {
            launchIO {
                audioEngine.onStop()
                _uiState.update { it.copy(playing = false) }
            }
        }
        lifecycle.doOnStart {
            launchIO {
                audioEngine.onStart()
            }
        }
    }

    fun dispatchIntent(intent: Intent) {
        when (intent) {
            Intent.PermissionsGranted -> {
                _uiState.update { it.copy(hasPermissions = true, loading = false) }
            }

            Intent.PermissionsDenied -> {
                _uiState.update { it.copy(loading = false) }
            }

            Intent.PlayPause -> {
                launchIO {
                    val result =
                        if (audioEngine.audioIsPlaying()) audioEngine.pauseAudio() else audioEngine.playAudio()
                    _uiState.update { it.copy(playing = result) }
                }
            }

            is Intent.SelectInputDevice -> {
                launchIO {
                    try {
                        audioEngine.setInputDevice(intent.deviceInfo.id)
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            }

            is Intent.SelectOutputDevice -> {
                launchIO {
                    audioEngine.setOutputDevice(intent.deviceInfo.id)
                }
            }

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
        }
    }

    data class State(
        val loading: Boolean = true,
        val hasPermissions: Boolean = false,
        val playing: Boolean = false,
        val inputDevices: List<AudioDeviceInfo> = emptyList(),
        val outputDevices: List<AudioDeviceInfo> = emptyList(),
    )

    sealed interface Intent {
        data object PermissionsGranted : Intent
        data object PermissionsDenied : Intent
        data object PlayPause : Intent
        data class SelectInputDevice(val deviceInfo: AudioDeviceInfo) : Intent
        data class SelectOutputDevice(val deviceInfo: AudioDeviceInfo) : Intent
        data class ChangeLeftChannel(val enabled: Boolean) : Intent
        data class ChangeRightChannel(val enabled: Boolean) : Intent
    }
}
