package com.sb.core.resources.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.vector.ImageVector
import com.sb.core.resources.icons.PauseIcon
import com.sb.core.resources.icons.PlayIcon

@Immutable
data class AppIcons(
    val play: ImageVector = PlayIcon,
    val pause: ImageVector = PauseIcon,
)

val baseAppIcons = AppIcons()

val LocalAppIcons = staticCompositionLocalOf<AppIcons> {
    error("Icons is not provided")
}