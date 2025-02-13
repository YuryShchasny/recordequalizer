package com.sb.core.resources.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

enum class ColorUiType {
    DARK,
    LIGHT
}

val White = Color(0xFFEAF0FF)
val Gray = Color(0xFF555B6A)
val GrayLight = Color(0xFFCACACA)
val BlueLight = Color(0xFFA5C0FF)
val BlueDark = Color(0xFF091227)
val Blue = Color(0xFF8996B8)

@Immutable
data class AppColors(
    val background: Color = White,
    val primary: Color = BlueDark,
    val secondary: Color = Blue,
    val white: Color = White,
    val gray: Color = Gray,
    val trackTintColor: Color = GrayLight,
)

fun darkColorScheme() = AppColors(
    background = BlueDark,
    primary = White,
    secondary = BlueLight.copy(0.7f),
    trackTintColor = Color.White.copy(0.31f)
)

fun lightColorScheme() = AppColors()

val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No ColorScheme provided")
}

val LocalColorUiType = staticCompositionLocalOf<ColorUiType> {
    error("No ColorUiType provided")
}

fun fetchColorScheme(colorUiType: ColorUiType): AppColors = when(colorUiType) {
    ColorUiType.DARK -> darkColorScheme()
    ColorUiType.LIGHT -> lightColorScheme()
}