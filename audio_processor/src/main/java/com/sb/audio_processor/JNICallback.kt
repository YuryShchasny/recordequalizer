package com.sb.audio_processor

interface JNICallback {
    fun onAudioDataReady(data: FloatArray)
}