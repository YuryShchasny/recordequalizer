package com.sb.core.resources.theme

import androidx.compose.runtime.staticCompositionLocalOf

enum class AppLanguage(val code: String) {
    RU("ru"),
    EN("en")
}

val LocalAppLanguage = staticCompositionLocalOf<AppLanguage> {
    error("Language is not provided")
}