package com.sb.core.resources.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.vector.ImageVector
import com.sb.core.resources.icons.MoonIcon
import com.sb.core.resources.icons.PauseIcon
import com.sb.core.resources.icons.PlayIcon
import com.sb.core.resources.icons.SlidersUpIcon
import com.sb.core.resources.icons.SunIcon

@Immutable
data class AppIcons(
    val play: ImageVector = PlayIcon,
    val pause: ImageVector = PauseIcon,
    val slidersUp: ImageVector = SlidersUpIcon,
    val moon: ImageVector = MoonIcon,
    val sun: ImageVector = SunIcon,
)

val baseAppIcons = AppIcons()

val LocalAppIcons = staticCompositionLocalOf<AppIcons> {
    error("Icons is not provided")
}