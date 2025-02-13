package com.sb.audio_processor

interface AudioEngine {
    fun onStart()
    fun onStop()
    suspend fun playAudio()
    suspend fun pauseAudio()
    suspend fun audioIsPlaying(): Boolean
    suspend fun setInputDevice(id: Int)
    suspend fun setOutputDevice(id: Int)
    suspend fun changeLeftChannel(enabled: Boolean)
    suspend fun changeRightChannel(enabled: Boolean)
}