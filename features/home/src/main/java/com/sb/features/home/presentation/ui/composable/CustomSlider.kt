package com.sb.features.home.presentation.ui.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Label
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sb.core.resources.AppRes
import com.sb.core.resources.theme.ColorUiType
import com.sb.core.resources.theme.EqualizerTheme
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSlider(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.Vertical,
    labelOffset: Dp = 0.dp
) {
    var sliderValue by remember { mutableFloatStateOf(value) }
    val interactionSource = remember { MutableInteractionSource() }
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
        if (orientation == Orientation.Vertical) {
            VerticalSlider(
                modifier = modifier.fillMaxWidth(),
                value = sliderValue,
                onValueChange = {
                    if (sliderValue != it) {
                        sliderValue = it
                        onValueChanged(it)
                    }
                },
                onValueChangeFinished = {
                    onValueChanged(sliderValue)
                },
                valueRange = valueRange,
                interactionSource = interactionSource,
                thumb = {
                    CustomThumb(
                        value = sliderValue,
                        color = AppRes.colors.primary,
                        interactionSource = interactionSource,
                        labelOffset = labelOffset
                    )
                },
                track = {
                    CustomTrack(
                        value = sliderValue,
                        valueRange = valueRange,
                        color = AppRes.colors.secondary.copy(1f),
                        tintColor = AppRes.colors.trackTintColor
                    )
                },
            )
        } else {
            Slider(
                modifier = modifier.fillMaxWidth(),
                value = sliderValue,
                onValueChange = {
                    if (sliderValue != it) {
                        sliderValue = it
                        onValueChanged(it)
                    }
                },
                onValueChangeFinished = {
                    onValueChanged(sliderValue)
                },
                valueRange = valueRange,
                interactionSource = interactionSource,
                thumb = {
                    CustomThumb(
                        value = sliderValue,
                        color = AppRes.colors.primary,
                        interactionSource = interactionSource,
                        labelOffset = labelOffset
                    )
                },
                track = {
                    CustomTrack(
                        value = sliderValue,
                        valueRange = valueRange,
                        color = AppRes.colors.secondary.copy(1f),
                        tintColor = AppRes.colors.trackTintColor
                    )
                },
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomThumb(
    value: Float,
    color: Color,
    interactionSource: MutableInteractionSource,
    labelOffset: Dp
) {
    Label(
        label = {
            PlainTooltip(
                modifier = Modifier
                    .size(65.dp, 25.dp)
                    .wrapContentWidth()
                    .offset(y = labelOffset),
                containerColor = AppRes.colors.secondary
            ) {
                Text(
                    "%.2f".format(value),
                    color = AppRes.colors.primary,
                    fontSize = 12.sp
                )
            }
        },
        interactionSource = interactionSource
    ) {
        Canvas(
            modifier = Modifier
                .width(16.dp)
                .height(16.dp)
        ) {
            drawCircle(
                color = color,
                radius = size.minDimension / 2,
                center = Offset(size.width / 2, size.height / 2)
            )
        }
    }
}

@Composable
private fun CustomTrack(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    color: Color,
    tintColor: Color
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
    ) {
        drawRoundRect(
            color = tintColor,
            size = Size(
                height = this.size.height,
                width = this.size.width
            ),
            cornerRadius = CornerRadius(12f, 12f)
        )
        drawRoundRect(
            color = color,
            size = Size(
                height = this.size.height,
                width = this.size.width * (value + valueRange.endInclusive) / abs(valueRange.endInclusive - valueRange.start)
            ),
            cornerRadius = CornerRadius(12f, 12f)
        )
    }
}

@Preview
@Composable
private fun FrequencySliderPreview() {
    EqualizerTheme(colorUiType = ColorUiType.DARK) {
        CustomSlider(value = 0.5f, valueRange = -10f..10f, onValueChanged = {})
    }
}
