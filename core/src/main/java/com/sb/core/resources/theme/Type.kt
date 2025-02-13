package com.sb.core.resources.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.sb.core.R

@Composable
private fun GilroyFamily() = FontFamily(
    fonts = listOf(
        Font(
            resId = R.font.gilroy_regular,
            weight = FontWeight(400),
            style = FontStyle.Normal,
        ),
        Font(
            resId = R.font.gilroy_medium,
            weight = FontWeight(500),
            style = FontStyle.Normal,
        ),
        Font(
            resId = R.font.gilroy_semibold,
            weight = FontWeight(600),
            style = FontStyle.Normal,
        ),
        Font(
            resId = R.font.gilroy_bold,
            weight = FontWeight(700),
            style = FontStyle.Normal,
        ),
    )
)

@Immutable
data class AppTypography(
    val gilroy: TextStyle,
    val gilroyMedium: TextStyle,
    val gilroySemibold: TextStyle,
    val gilroyBold: TextStyle,
)

val LocalAppTypography = staticCompositionLocalOf<AppTypography> {
    error("Core typography not found")
}

@Composable
fun AppTypography() = AppTypography(
    gilroy = TextStyle(
        fontFamily = GilroyFamily(),
        fontWeight = FontWeight(400),
        fontStyle = FontStyle.Normal
    ),
    gilroyMedium = TextStyle(
        fontFamily = GilroyFamily(),
        fontWeight = FontWeight(500),
        fontStyle = FontStyle.Normal
    ),
    gilroySemibold = TextStyle(
        fontFamily = GilroyFamily(),
        fontWeight = FontWeight(600),
        fontStyle = FontStyle.Normal
    ),
    gilroyBold = TextStyle(
        fontFamily = GilroyFamily(),
        fontWeight = FontWeight(700),
        fontStyle = FontStyle.Normal
    ),
)
