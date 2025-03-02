package com.sb.features.home.presentation.ui.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sb.core.resources.AppRes

@Composable
fun DashedLine(modifier: Modifier = Modifier, label: String = String()) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            color = AppRes.colors.secondary,
            fontSize = 8.sp,
            style = AppRes.type.gilroy
        )
        val lineColor = AppRes.colors.trackTintColor.copy(0.6f)
        Canvas(
            modifier = Modifier
                .weight(1f)
                .height(16.dp)
        ) {
            drawLine(
                color = lineColor,
                start = Offset(x = 0f, size.height / 2),
                end = Offset(x = size.width, y = size.height / 2),
                cap = StrokeCap.Round,
                strokeWidth = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
        }
    }
}