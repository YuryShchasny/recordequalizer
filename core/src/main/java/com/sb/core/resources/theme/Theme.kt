package com.sb.core.resources.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun EqualizerTheme(
    language: AppLanguage = AppLanguage.RU,
    colorUiType: ColorUiType = ColorUiType.LIGHT,
    content: @Composable () -> Unit
) {
    val colorScheme = fetchColorScheme(colorUiType)
    val appStrings = fetchCoreStrings(language)
    MaterialTheme(
        colorScheme = if (colorUiType == ColorUiType.LIGHT) {
            lightColorScheme()
        } else {
            darkColorScheme()
        },
    ) {
        CompositionLocalProvider(
            LocalAppLanguage provides language,
            LocalAppColors provides colorScheme,
            LocalAppStrings provides appStrings,
            LocalAppIcons provides baseAppIcons,
            LocalAppTypography provides AppTypography(),
            LocalColorUiType provides colorUiType,
            content = content
        )
    }
}