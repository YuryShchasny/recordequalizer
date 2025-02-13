package com.sb.core.resources.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
data class AppStrings(
    val appName: String,
    val requestPermission: String,
    val leftChannel: String,
    val rightChannel: String,
    val recordDevice: String,
    val playbackDevice: String,
    val default: String,
)

internal val russianAppStrings = AppStrings(
    appName = "Эквалайзер",
    requestPermission = "Предоставьте доступ к вашему микрофону для работы приложения",
    leftChannel = "Левый канал",
    rightChannel = "Правый канал",
    recordDevice = "Записывающее устройство",
    playbackDevice = "Устройство воспроизведения",
    default = "По умолчанию",
)

internal val englishAppStrings = AppStrings(
    appName = "Equalizer",
    requestPermission = "Provide access to your microphone for the application of the application",
    leftChannel = "Left channel",
    rightChannel = "Right channel",
    recordDevice = "Record device",
    playbackDevice = "Playback device",
    default = "Default",
)

val LocalAppStrings = staticCompositionLocalOf<AppStrings> {
    error("Core Strings is not provided")
}

fun fetchCoreStrings(appLanguage: AppLanguage) = when (appLanguage) {
    AppLanguage.RU -> russianAppStrings
    AppLanguage.EN -> englishAppStrings
}