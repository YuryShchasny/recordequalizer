package com.sb.domain.entity

import android.net.Uri

data class AudioServiceState(
    val queue: List<Uri> = emptyList(),
    val selectedTrack: Uri? = null,
    val isPlaying: Boolean = false,
    val positionMs: Long = 0L,
    val hasVolume: Boolean = true,
    val repeatMode: Int = 0,
    val shuffled: Boolean = false,
)
