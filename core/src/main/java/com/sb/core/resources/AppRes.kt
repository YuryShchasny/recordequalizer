package com.sb.core.resources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import com.sb.core.resources.theme.AppColors
import com.sb.core.resources.theme.AppIcons
import com.sb.core.resources.theme.AppTypography
import com.sb.core.resources.theme.ColorUiType
import com.sb.core.resources.theme.LocalAppColors
import com.sb.core.resources.theme.LocalAppIcons
import com.sb.core.resources.theme.LocalAppTypography
import com.sb.core.resources.theme.LocalColorUiType


@Immutable
object AppRes {
    val colors: AppColors
        @[Composable ReadOnlyComposable] get() = LocalAppColors.current

    val icons: AppIcons
        @[Composable ReadOnlyComposable] get() = LocalAppIcons.current

    val type: AppTypography
        @[Composable ReadOnlyComposable] get() = LocalAppTypography.current

    val theme: ColorUiType
        @[Composable ReadOnlyComposable] get() = LocalColorUiType.current
}