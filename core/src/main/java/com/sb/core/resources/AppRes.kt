package com.sb.core.resources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import com.sb.core.resources.theme.AppColors
import com.sb.core.resources.theme.AppIcons
import com.sb.core.resources.theme.AppLanguage
import com.sb.core.resources.theme.AppStrings
import com.sb.core.resources.theme.AppTypography
import com.sb.core.resources.theme.ColorUiType
import com.sb.core.resources.theme.LocalAppColors
import com.sb.core.resources.theme.LocalAppIcons
import com.sb.core.resources.theme.LocalAppLanguage
import com.sb.core.resources.theme.LocalAppStrings
import com.sb.core.resources.theme.LocalAppTypography
import com.sb.core.resources.theme.LocalColorUiType


@Immutable
object AppRes {
    val colors: AppColors
        @[Composable ReadOnlyComposable] get() = LocalAppColors.current

    val language: AppLanguage
        @[Composable ReadOnlyComposable] get() = LocalAppLanguage.current

    val strings: AppStrings
        @[Composable ReadOnlyComposable] get() = LocalAppStrings.current

    val icons: AppIcons
        @[Composable ReadOnlyComposable] get() = LocalAppIcons.current

    val type: AppTypography
        @[Composable ReadOnlyComposable] get() = LocalAppTypography.current

    val theme: ColorUiType
        @[Composable ReadOnlyComposable] get() = LocalColorUiType.current
}