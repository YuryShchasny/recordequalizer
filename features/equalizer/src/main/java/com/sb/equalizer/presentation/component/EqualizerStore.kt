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
            profilesRepository.getSelectedProfile()?.let {
                updateSelectedProfile(it)
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

    fun dispatchIntent(intent: Intent) {
        launchMain {
            when (intent) {
                is Intent.ChangeLeftChannel -> changeLeftChannel(intent.enabled)
                is Intent.ChangeRightChannel -> changeRightChannel(intent.enabled)
                is Intent.FrequencyGainChanged -> changeFrequencyGain(
                    intent.frequency,
                    intent.value
                )

                is Intent.AmplitudeGainChanged -> changeAmplitude(intent.value)
                is Intent.ChangeProfile -> changeProfile(intent.profile)
                is Intent.SaveNewProfile -> saveProfile(intent.name)
                is Intent.DeleteProfile -> deleteProfile(intent.profile)
                is Intent.EnableCompressor -> enableCompressor(intent.enabled)
            }
        }
    }

    private suspend fun changeLeftChannel(enabled: Boolean) {
        _uiState.update {
            it?.copy(leftChannelEnabled = enabled)
        }
        audioEngine.changeLeftChannel(enabled)
    }

    private suspend fun changeRightChannel(enabled: Boolean) {
        _uiState.update {
            it?.copy(rightChannelEnabled = enabled)
        }
        audioEngine.changeRightChannel(enabled)
    }

    private suspend fun changeFrequencyGain(frequency: Int, value: Float) {
        _uiState.update { state ->
            state?.copy(
                frequencies = state.frequencies.map { frequencyPair ->
                    if (frequencyPair.first == frequency) {
                        frequencyPair.copy(second = value)
                    } else frequencyPair
                }
            )
        }
        audioEngine.setFrequencyGain(frequency, value)
    }

    private suspend fun changeAmplitude(value: Float) {
        _uiState.update { state ->
            state?.copy(
                amplitude = value
            )
        }
        audioEngine.setAmplitudeGain(value)
    }

    private suspend fun changeProfile(profile: Profile) {
        profilesRepository.setSelectedProfile(profile.id)
        updateSelectedProfile(profile)
    }

    private suspend fun saveProfile(name: String) {
        state.value?.let { state ->
            val newProfile = Profile(
                name = name,
                gains = state.frequencies.map { it.second },
                amplitude = state.amplitude,
                leftChannel = state.leftChannelEnabled,
                rightChannel = state.rightChannelEnabled,
                compressorEnabled = state.compressorEnabled,
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

    private suspend fun deleteProfile(profile: Profile) {
        profilesRepository.deleteProfile(profile.id)
        _uiState.update {
            it?.copy(
                profiles = profilesRepository.getProfiles()
            )
        }
    }

    private suspend fun enableCompressor(enabled: Boolean) {
        _uiState.update {
            it?.copy(
                compressorEnabled = enabled
            )
        }
        audioEngine.enableCompressor(enabled)
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
                compressorEnabled = profile.compressorEnabled
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
        val compressorEnabled: Boolean,
    )

    sealed interface Intent {
        data class ChangeProfile(val profile: Profile) : Intent
        data class ChangeLeftChannel(val enabled: Boolean) : Intent
        data class ChangeRightChannel(val enabled: Boolean) : Intent
        data class FrequencyGainChanged(val frequency: Int, val value: Float) : Intent
        data class AmplitudeGainChanged(val value: Float) : Intent
        data class EnableCompressor(val enabled: Boolean) : Intent
        data class SaveNewProfile(val name: String) : Intent
        data class DeleteProfile(val profile: Profile) : Intent
    }
}
