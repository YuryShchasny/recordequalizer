package com.sb.audio_processor

import android.content.Context
import android.media.AudioManager
import com.sb.domain.entity.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext


class NativeAudioEngine(context: Context) : AudioEngine {
    private val mutex = Mutex()

    private external fun create(): Boolean
    private external fun delete()
    private external fun play()
    private external fun stop()
    private external fun isPlaying(): Boolean
    private external fun setRecordingDeviceId(deviceId: Int)
    private external fun setPlaybackDeviceId(deviceId: Int)
    private external fun setDefaultStreamValues(sampleRate: Int, framesPerBurst: Int)
    private external fun nativeInitializeEqualizer(
        amplitude: Float,
        frequenciesSize: Int,
        frequencies: IntArray,
        gains: FloatArray,
        leftChannel: Boolean,
        rightChannel: Boolean
    )

    private external fun nativeSetProfile(
        amplitude: Float,
        gains: FloatArray,
        leftChannel: Boolean,
        rightChannel: Boolean
    )

    private external fun nativeSetAmplitudeGain(gain: Float)
    private external fun nativeSetFrequencyGain(frequency: Int, gain: Float)
    private external fun nativeChangeLeftChannel(enabled: Boolean)
    private external fun nativeChangeRightChannel(enabled: Boolean)
    private external fun nativeAddListener(callback: JNICallback)

    init {
        setDefaultStreamValues(context)
    }

    override suspend fun onCreate() {
        mutex.withLock {
            create()
        }
    }

    override suspend fun onDestroy() {
        mutex.withLock {
            delete()
        }
    }

    override suspend fun playAudio(): Boolean = withContext(Dispatchers.Default) {
        mutex.withLock {
            if (!isPlaying()) {
                val job = launch { play() }
                job.join()
                return@withContext isPlaying()
            }
            return@withContext true
        }
    }

    override suspend fun pauseAudio() = withContext(Dispatchers.Default) {
        mutex.withLock {
            if (isPlaying()) {
                val job = launch { stop() }
                job.join()
                return@withContext isPlaying()
            }
            return@withContext false
        }
    }

    override suspend fun audioIsPlaying(): Boolean = withContext(Dispatchers.Default) {
        return@withContext isPlaying()
    }

    override suspend fun setInputDevice(id: Int) = withContext(Dispatchers.Default) {
        mutex.withLock {
            setRecordingDeviceId(id)
        }
    }

    override suspend fun setOutputDevice(id: Int) = withContext(Dispatchers.Default) {
        mutex.withLock {
            setPlaybackDeviceId(id)
        }
    }

    override suspend fun changeLeftChannel(enabled: Boolean) = withContext(Dispatchers.Default) {
        mutex.withLock {
            nativeChangeLeftChannel(enabled)
        }
    }

    override suspend fun changeRightChannel(enabled: Boolean) = withContext(Dispatchers.Default) {
        mutex.withLock {
            nativeChangeRightChannel(enabled)
        }
    }

    override suspend fun setFrequencyGain(frequency: Int, value: Float) =
        withContext(Dispatchers.Default) {
            mutex.withLock {
                nativeSetFrequencyGain(frequency, value)
            }
        }

    override suspend fun setAmplitudeGain(value: Float) = withContext(Dispatchers.Default) {
        mutex.withLock {
            nativeSetAmplitudeGain(value)
        }
    }

    override suspend fun initEqualizer(profile: Profile, frequencies: List<Int>) =
        withContext(Dispatchers.Default) {
            mutex.withLock {
                nativeInitializeEqualizer(
                    amplitude = profile.amplitude,
                    frequenciesSize = frequencies.size,
                    frequencies = frequencies.toIntArray(),
                    gains = profile.gains.toFloatArray(),
                    leftChannel = profile.leftChannel,
                    rightChannel = profile.rightChannel,
                )
            }
        }

    override suspend fun setProfile(profile: Profile) {
        nativeSetProfile(
            amplitude = profile.amplitude,
            gains = profile.gains.toFloatArray(),
            leftChannel = profile.leftChannel,
            rightChannel = profile.rightChannel
        )
    }

    override suspend fun addAudioDataListener(callback: JNICallback) =
        withContext(Dispatchers.Default) {
            mutex.withLock {
                nativeAddListener(callback)
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
