package com.sb.audio_processor

import android.content.Context
import android.media.AudioManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class NativeAudioEngine(context: Context) : AudioEngine {
    private val synthesizerMutex = Object()

    private external fun create(): Boolean
    private external fun delete()
    private external fun play()
    private external fun stop()
    private external fun isPlaying(): Boolean
    private external fun setRecordingDeviceId(deviceId: Int)
    private external fun setPlaybackDeviceId(deviceId: Int)
    private external fun setDefaultStreamValues(sampleRate: Int, framesPerBurst: Int)
    private external fun nativeChangeLeftChannel(enabled: Boolean)
    private external fun nativeChangeRightChannel(enabled: Boolean)

    init {
        setDefaultStreamValues(context)
    }

    override fun onStart() {
        synchronized(synthesizerMutex) {
            create()
        }
    }

    override fun onStop() {
        synchronized(synthesizerMutex) {
            delete()
        }
    }

    override suspend fun playAudio() = withContext(Dispatchers.Default) {
        synchronized(synthesizerMutex) {
            play()
        }
    }

    override suspend fun pauseAudio() = withContext(Dispatchers.Default) {
        synchronized(synthesizerMutex) {
            stop()
        }
    }

    override suspend fun audioIsPlaying(): Boolean = withContext(Dispatchers.Default) {
        synchronized(synthesizerMutex) {
            return@withContext isPlaying()
        }
    }

    override suspend fun setInputDevice(id: Int) {
        synchronized(synthesizerMutex) {
            setRecordingDeviceId(id)
        }
    }

    override suspend fun setOutputDevice(id: Int) {
        synchronized(synthesizerMutex) {
            setPlaybackDeviceId(id)
        }
    }

    override suspend fun changeLeftChannel(enabled: Boolean) {
        synchronized(synthesizerMutex) {
            nativeChangeLeftChannel(enabled)
        }
    }

    override suspend fun changeRightChannel(enabled: Boolean) {
        synchronized(synthesizerMutex) {
            nativeChangeRightChannel(enabled)
        }
    }

    private fun setDefaultStreamValues(context: Context) {
            val myAudioMgr = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val sampleRateStr = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE)
            val defaultSampleRate = sampleRateStr.toInt()
            val framesPerBurstStr =
                myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER)
            val defaultFramesPerBurst = framesPerBurstStr.toInt()
            setDefaultStreamValues(defaultSampleRate, defaultFramesPerBurst)
    }
}
