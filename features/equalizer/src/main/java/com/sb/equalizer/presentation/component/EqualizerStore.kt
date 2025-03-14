package com.sb.equalizer.presentation.component

import androidx.compose.runtime.Immutable
import com.sb.audio_processor.AudioEngine
import com.sb.core.base.BaseStore
import com.sb.domain.entity.Profile
import com.sb.domain.repository.ProfilesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.inject


class EqualizerStore : BaseStore() {

    private val profilesRepository by inject<ProfilesRepository>()
    private val audioEngine by inject<AudioEngine>()

    private var _uiState = MutableStateFlow<State?>(null)
    val state = _uiState.asStateFlow()

    init {
        launchIO {
            val profile = profilesRepository.getSelectedProfile()
            profile?.let { updateSelectedProfile(it) }
        }
    }

    fun dispatchIntent(intent: Intent) {
        when (intent) {
            is Intent.ChangeLeftChannel -> {
                launchIO {
                    _uiState.update {
                        it?.copy(leftChannelEnabled = intent.enabled)
                    }
                    audioEngine.changeLeftChannel(intent.enabled)
                }
            }

            is Intent.ChangeRightChannel -> {
                launchIO {
                    _uiState.update {
                        it?.copy(rightChannelEnabled = intent.enabled)
                    }
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

            is Intent.ChangeProfile -> {
                launchIO {
                    profilesRepository.setSelectedProfile(intent.profile.id)
                    updateSelectedProfile(intent.profile)
                }
            }

            is Intent.SaveNewProfile -> {
                launchIO {
                    state.value?.let { state ->
                        val newProfile = Profile(
                            name = intent.name,
                            gains = state.frequencies.map { it.second },
                            amplitude = state.amplitude,
                            leftChannel = state.leftChannelEnabled,
                            rightChannel = state.rightChannelEnabled
                        )
                        profilesRepository.addProfile(newProfile)
                        val profiles = profilesRepository.getProfiles()
                        val selectedProfile = profiles.firstOrNull { it.name == newProfile.name }
                        selectedProfile?.let {
                            profilesRepository.setSelectedProfile(it.id)
                            updateSelectedProfile(selectedProfile)
                        }
                    }
                }
            }

            is Intent.DeleteProfile -> {
                launchIO {
                    profilesRepository.deleteProfile(intent.profile.id)
                    _uiState.update {
                        it?.copy(
                            profiles = profilesRepository.getProfiles()
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        CoroutineScope(Dispatchers.Main).launch {
            val profile = profilesRepository.getSelectedProfile()
            profile?.let { audioEngine.setProfile(it) }
        }
        super.onDestroy()
    }

    private suspend fun updateSelectedProfile(profile: Profile) {
        val frequencies =
            profilesRepository.getDefaultFrequencies().map { it.first }.zip(profile.gains)
        val profiles = profilesRepository.getProfiles()
        _uiState.update {
            State(
                selectedProfile = profile,
                profiles = profiles,
                amplitude = profile.amplitude,
                frequencies = frequencies,
                leftChannelEnabled = profile.leftChannel,
                rightChannelEnabled = profile.rightChannel,
            )
        }
        audioEngine.setProfile(profile)
    }

    @Immutable
    data class State(
        val selectedProfile: Profile,
        val profiles: List<Profile>,
        val frequencies: List<Pair<Int, Float>>,
        val amplitude: Float,
        val leftChannelEnabled: Boolean,
        val rightChannelEnabled: Boolean,
    )

    sealed interface Intent {
        data class ChangeProfile(val profile: Profile) : Intent
        data class ChangeLeftChannel(val enabled: Boolean) : Intent
        data class ChangeRightChannel(val enabled: Boolean) : Intent
        data class FrequencyGainChanged(val frequency: Int, val value: Float) : Intent
        data class AmplitudeGainChanged(val value: Float) : Intent
        data class SaveNewProfile(val name: String) : Intent
        data class DeleteProfile(val profile: Profile) : Intent
    }
}
