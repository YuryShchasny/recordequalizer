package com.sb.home.presentation.ui.composable

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sb.core.resources.AppRes

@Composable
fun Waveform(
    modifier: Modifier = Modifier,
    amplitudes: List<Float>,
    color: Color = AppRes.colors.secondary.copy(1f),
    rectWidth: Dp = 2.dp,
    scale: Float = 1.0f,
) {
    val trackColor = AppRes.colors.gray
    val red = AppRes.colors.red
    Canvas(modifier = modifier) {
        drawRect(
            color = trackColor,
            size = Size(width = size.width, height = 1.dp.toPx()),
            topLeft = Offset(0f, size.height / 2)
        )
        drawLine(
            color = trackColor,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
        drawLine(
            color = trackColor,
            start = Offset(0f, size.height),
            end = Offset(size.width, size.height),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
        amplitudes.reversed().forEachIndexed { index, amplitude ->
            val offsetX =
                this.size.width / 2 - ((rectWidth.toPx() + 0.2f) * index) - rectWidth.toPx()
            if (offsetX >= 0) {
                val height = (this.size.height * amplitude * scale).coerceIn(0f, this.size.height)
                if (height > 8) {
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(
                            x = offsetX,
                            y = (size.height / 2) - (height / 2)
                        ),
                        size = Size(
                            width = rectWidth.value,
                            height = height
                        ),
                        cornerRadius = CornerRadius(x = rectWidth.value, y = rectWidth.value),
                    )
                }
            } else return@forEachIndexed
        }
        drawLine(
            color = red,
            start = Offset(size.width / 2, size.height / 4),
            end = Offset(size.width / 2, size.height * 3 / 4),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}