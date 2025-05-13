package com.sb.core.resources.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun EqualizerTheme(
    colorUiType: ColorUiType = ColorUiType.LIGHT,
    content: @Composable () -> Unit
) {
    val colorScheme = fetchColorScheme(colorUiType)
    MaterialTheme(
        colorScheme = if (colorUiType == ColorUiType.LIGHT) {
            lightColorScheme()
        } else {
            darkColorScheme()
        },
    ) {
        CompositionLocalProvider(
            LocalAppColors provides colorScheme,
            LocalAppIcons provides baseAppIcons,
            LocalAppTypography provides AppTypography(),
            LocalColorUiType provides colorUiType,
            content = content
        )
    }
}