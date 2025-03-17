package com.sb.data.local.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class ProfileDbo(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val gains: List<Float>,
    val amplitude: Float,
    val leftChannel: Boolean,
    val rightChannel: Boolean,
    val compressorEnabled: Boolean,
)
