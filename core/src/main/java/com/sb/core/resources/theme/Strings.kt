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
    val gainAmplitude: String,
    val profile: String,
    val save: String,
    val cancel: String,
    val newProfileName: String,
    val saveNewProfile: String,
    val profileExists: String,
    val errorSelectDevice: String,
)

internal val russianAppStrings = AppStrings(
    appName = "Эквалайзер",
    requestPermission = "Предоставьте доступ к вашему микрофону для работы приложения",
    leftChannel = "Левый канал",
    rightChannel = "Правый канал",
    recordDevice = "Записывающее устройство",
    playbackDevice = "Устройство воспроизведения",
    default = "По умолчанию",
    gainAmplitude = "Усиление амплитуды",
    profile = "Профиль",
    save = "Сохранить",
    cancel = "Отмена",
    newProfileName = "Название нового профиля",
    saveNewProfile = "Сохранить новый профиль",
    profileExists = "Данный профиль уже существует, измените название",
    errorSelectDevice = "Не удалось выбрать устройство"
)

internal val englishAppStrings = AppStrings(
    appName = "Equalizer",
    requestPermission = "Provide access to your microphone for the application of the application",
    leftChannel = "Left channel",
    rightChannel = "Right channel",
    recordDevice = "Record device",
    playbackDevice = "Playback device",
    default = "Default",
    gainAmplitude = "Gain amplitude",
    profile = "Profile",
    save = "Save",
    cancel = "Cancel",
    newProfileName = "New profile name",
    saveNewProfile = "Save new profile",
    profileExists = "This profile already exists, change the name",
    errorSelectDevice = "Device selection failed"
)

val LocalAppStrings = staticCompositionLocalOf<AppStrings> {
    error("Core Strings is not provided")
}

fun fetchCoreStrings(appLanguage: AppLanguage) = when (appLanguage) {
    AppLanguage.RU -> russianAppStrings
    AppLanguage.EN -> englishAppStrings
}