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
    val errorPlay: String,
    val compressorEffect: String,
    val stopListening: String,
    val stopRecording: String,
    val startListening: String,
    val startRecording: String,
    val myFolder: String,
    val saveRecordError: String,
    val saveRecordSuccess: String,
    val selectDeviceError: String,
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
    errorPlay = "Ошибка при запуске. Попробуйте еще раз.",
    compressorEffect = "Эффект компрессии",
    stopListening = "Остановить прослушивание",
    stopRecording = "Остановить запись",
    startRecording = "Начать запись",
    startListening = "Начать просушивание",
    myFolder = "Мои записи",
    saveRecordError = "Не удалось сохранить запись",
    saveRecordSuccess = "Запись сохранена в папке Recordings/RecordEqualizer",
    selectDeviceError = "Остановите воспроизведение для смены устройства"
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
    errorPlay = "Error when starting. Try again.",
    compressorEffect = "Compression effect",
    stopListening = "Stop listening",
    stopRecording = "Stop recording",
    startListening = "Start listening",
    startRecording = "Start recording",
    myFolder = "My records",
    saveRecordError = "Failed to save the record",
    saveRecordSuccess = "Record is saved in the Recordings/RecordEqualizer folder",
    selectDeviceError = "Stop playing to change the device"
)

val LocalAppStrings = staticCompositionLocalOf<AppStrings> {
    error("Core Strings is not provided")
}

fun fetchCoreStrings(appLanguage: AppLanguage) = when (appLanguage) {
    AppLanguage.RU -> russianAppStrings
    AppLanguage.EN -> englishAppStrings
}