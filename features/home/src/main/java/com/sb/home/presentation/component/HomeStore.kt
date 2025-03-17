package com.sb.home.presentation.component

import android.content.ContentValues
import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.compose.runtime.Immutable
import com.sb.audio_processor.AudioEngine
import com.sb.audio_processor.JNICallback
import com.sb.core.base.BaseStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.component.inject
import java.io.File
import java.io.FileInputStream
import kotlin.math.abs


class HomeStore : BaseStore() {

    private val context by inject<Context>()

    private val audioEngine by inject<AudioEngine>()

    private var _uiState = MutableStateFlow<State?>(null)
    val state = _uiState.asStateFlow()

    private var _messages = MutableSharedFlow<Messages>()
    val messages = _messages.asSharedFlow()

    private var listenerJob: Job? = null
    private var buffer = mutableListOf<Float>()
    private val listener = object : JNICallback {
        override fun onAudioDataReady(data: FloatArray) {
            buffer = buffer.apply {
                add(data.average().toFloat())
                takeLast(1024)
            }
        }
    }

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
        listenerJob = launchIO {
            while (true) {
                delay(50)
                if (buffer.isNotEmpty()) {
                    val bufferCopy = mutableListOf<Float>()
                    bufferCopy.addAll(buffer)
                    _uiState.update {
                        it?.copy(
                            streamAmplitudes = it.streamAmplitudes + listOfNotNull(bufferCopy.maxOfOrNull { value ->
                                abs(value)
                            })
                        )
                    }
                    buffer.clear()
                } else {
                    _uiState.update {
                        it?.copy(
                            streamAmplitudes = it.streamAmplitudes + listOfNotNull(0f)
                        )
                    }
                }
            }
        }
    }

    fun initListener() {
        launchMain {
            audioEngine.addAudioDataListener(listener)
        }
    }

    fun dispatchIntent(intent: Intent) {
        when (intent) {
            is Intent.SelectInputDevice -> {
                launchIO {
                    audioEngine.setInputDevice(intent.deviceInfo.id)
                    _uiState.update {
                        it?.copy(
                            selectedInputDevice = intent.deviceInfo
                        )
                    }
                }
            }

            is Intent.SelectOutputDevice -> {
                launchIO {
                    audioEngine.setOutputDevice(intent.deviceInfo.id)
                    _uiState.update {
                        it?.copy(
                            selectedOutputDevice = intent.deviceInfo
                        )
                    }
                }
            }

            Intent.ListenClick -> {
                launchIO {
                    val result = if (audioEngine.audioIsPlaying()) {
                        audioEngine.pauseAudio()
                    } else {
                        play(false)
                    }
                    _uiState.update {
                        it?.copy(
                            playing = result,
                            recordMode = false
                        )
                    }
                }
            }

            Intent.RecordClick -> {
                launchIO {
                    val result = if (audioEngine.audioIsPlaying()) {
                        val pauseResult = audioEngine.pauseAudio()
                        saveRecord()
                        pauseResult
                    } else {
                        play(true)
                    }
                    _uiState.update {
                        it?.copy(
                            playing = result,
                            recordMode = true
                        )
                    }
                }
            }
        }
    }

    private suspend fun play(withRecord: Boolean): Boolean {
        return try {
            audioEngine.playAudio(withRecord)
        } catch (e: Throwable) {
            _messages.emit(Messages.PlayError)
            _uiState.update {
                it?.copy(
                    playing = false
                )
            }
            e.printStackTrace()
            false
        }
    }

    private fun saveRecord() {
        val internalDir = File(context.filesDir, "records")
        val externalDir =
            File(Environment.getExternalStorageDirectory(), "Recordings/RecordEqualizer")
        if (!externalDir.exists()) {
            externalDir.mkdirs()
        }
        if (internalDir.exists() && internalDir.isDirectory) {
            internalDir.listFiles()?.forEach { file ->
                val newFile = File(externalDir, file.name)
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        copyFileToPublicDir(file, newFile)
                    } else {
                        file.copyTo(newFile, overwrite = true)
                    }
                    file.delete()
                    launchMain {
                        _messages.emit(Messages.SaveRecordSuccess)
                    }
                } catch (e: Exception) {
                    launchMain {
                        _messages.emit(Messages.SaveRecordError)
                    }
                    e.printStackTrace()
                }
            }
        }
    }

    private fun copyFileToPublicDir(sourceFile: File, newFile: File) {
        val extension = sourceFile.extension.lowercase()
        val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, newFile.name)
            put(MediaStore.MediaColumns.MIME_TYPE, mime ?: "audio/wav")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Recordings/RecordEqualizer/")
        }

        val uri = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                FileInputStream(sourceFile).use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
    }

    @Immutable
    data class State(
        val playing: Boolean = false,
        val recordMode: Boolean = false,
        val selectedInputDevice: AudioDeviceInfo? = null,
        val selectedOutputDevice: AudioDeviceInfo? = null,
        val inputDevices: List<AudioDeviceInfo> = emptyList(),
        val outputDevices: List<AudioDeviceInfo> = emptyList(),
        val streamAmplitudes: List<Float> = emptyList(),
    )

    sealed interface Intent {
        data object ListenClick : Intent
        data object RecordClick : Intent
        data class SelectInputDevice(val deviceInfo: AudioDeviceInfo) : Intent
        data class SelectOutputDevice(val deviceInfo: AudioDeviceInfo) : Intent
    }

    sealed interface Messages {
        data object PlayError : Messages
        data object SaveRecordSuccess: Messages
        data object SaveRecordError: Messages
    }
}
