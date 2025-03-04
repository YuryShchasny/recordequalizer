package com.sb.core.composable

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.sb.core.resources.AppRes

@Composable
fun ClickableIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    tint: Color,
    rippleRadius: Dp,
    onClick: () -> Unit,
) {
    Icon(
        modifier = modifier
            .clickable(
                indication = ripple(
                    radius = rippleRadius,
                    bounded = false,
                    color = AppRes.colors.primary
                ),
                interactionSource = null
            ) {
                onClick()
            },
        imageVector = imageVector,
        contentDescription = null,
        tint = tint
    )
}
