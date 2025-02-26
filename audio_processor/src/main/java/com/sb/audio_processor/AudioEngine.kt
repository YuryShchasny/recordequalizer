package com.sb.audio_processor

interface AudioEngine {
    suspend fun onStart()
    suspend fun onStop()
    suspend fun playAudio(): Boolean
    suspend fun pauseAudio(): Boolean
    suspend fun audioIsPlaying(): Boolean
    suspend fun setInputDevice(id: Int)
    suspend fun setOutputDevice(id: Int)
    suspend fun changeLeftChannel(enabled: Boolean)
    suspend fun changeRightChannel(enabled: Boolean)
    suspend fun setFrequencyGain(frequency: Int, value: Float)
    suspend fun initEqualizer(frequencies: List<Pair<Int, Float>>)
}