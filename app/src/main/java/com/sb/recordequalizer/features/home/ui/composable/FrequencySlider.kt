package com.sb.recordequalizer.features.home.ui.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Label
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.PlainTooltip
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sb.core.resources.AppRes
import com.sb.core.resources.theme.ColorUiType
import com.sb.core.resources.theme.EqualizerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrequencySlider(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val thumbColor = AppRes.colors.primary
    val trackActiveColor = AppRes.colors.secondary.copy(1f)
    val trackInactiveColor = AppRes.colors.trackTintColor
    var sliderValue by remember { mutableFloatStateOf(value) }
    val interactionSource = remember { MutableInteractionSource() }
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
        VerticalSlider(
            modifier = modifier.fillMaxWidth(),
            value = sliderValue,
            onValueChange = {
                if(sliderValue != it) {
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
                Label(
                    label = {
                        PlainTooltip(
                            modifier = Modifier.offset(y = (-16).dp),
                            containerColor = AppRes.colors.secondary
                        ) {
                            Text(
                                "%.2f".format(sliderValue),
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
                            color = thumbColor,
                            radius = size.minDimension / 2,
                            center = Offset(size.width / 2, size.height / 2)
                        )
                    }
                }
            },
            track = {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                ) {
                    drawRoundRect(
                        color = trackInactiveColor,
                        size = Size(
                            height = this.size.height,
                            width = this.size.width
                        ),
                        cornerRadius = CornerRadius(12f, 12f)
                    )
                    drawRoundRect(
                        color = trackActiveColor,
                        size = Size(
                            height = this.size.height,
                            width = this.size.width * (sliderValue + 10) / 20
                        ),
                        cornerRadius = CornerRadius(12f, 12f)
                    )
                }
            },
        )
    }
}

@Preview
@Composable
private fun FrequencySliderPreview() {
    EqualizerTheme(colorUiType = ColorUiType.DARK) {
        FrequencySlider(value = 0.5f, valueRange = -10f..10f,  onValueChanged = {})
    }
}
