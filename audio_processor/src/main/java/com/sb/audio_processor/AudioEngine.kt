package com.sb.audio_processor

import com.sb.domain.entity.Profile

interface AudioEngine {
    suspend fun onCreate()
    suspend fun onDestroy()
    suspend fun playAudio(): Boolean
    suspend fun pauseAudio(): Boolean
    suspend fun audioIsPlaying(): Boolean
    suspend fun setInputDevice(id: Int)
    suspend fun setOutputDevice(id: Int)
    suspend fun changeLeftChannel(enabled: Boolean)
    suspend fun changeRightChannel(enabled: Boolean)
    suspend fun setFrequencyGain(frequency: Int, value: Float)
    suspend fun setAmplitudeGain(value: Float)
    suspend fun initEqualizer(profile: Profile, frequencies: List<Int>)
    suspend fun setProfile(profile: Profile)
}