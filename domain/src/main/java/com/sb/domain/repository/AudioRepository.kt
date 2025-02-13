package com.sb.domain.repository

import com.sb.domain.entity.AudioFile
import com.sb.domain.entity.AudioServiceState

interface AudioRepository {
    suspend fun getAudioFiles(): List<AudioFile>
    suspend fun getPlayerState(): AudioServiceState
    suspend fun savePlayerState(audioServiceState: AudioServiceState)
}
