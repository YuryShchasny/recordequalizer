package com.sb.features.home.presentation.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sb.core.resources.AppRes
import com.sb.core.resources.theme.ColorUiType
import com.sb.core.resources.theme.EqualizerTheme

@Composable
fun Equalizer(
    frequencies: List<Pair<Int, Float>>,
    valueRange: ClosedFloatingPointRange<Float>,
    onGainChanged: (Int, Float) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 400.dp
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        ) {
            DashedLinesColumn(
                height = height,
                startValue = valueRange.endInclusive.toInt(),
                endValue = valueRange.start.toInt()
            )
            Row(
                modifier = Modifier
                    .matchParentSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                frequencies.forEach { frequency ->
                    CustomSlider(
                        modifier = Modifier.weight(1f),
                        value = frequency.second,
                        valueRange = valueRange,
                        onValueChanged = { newValue -> onGainChanged(frequency.first, newValue) },
                        labelOffset = (-16).dp
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            frequencies.forEach { frequency ->
                val label = if (frequency.first >= 1000) "${(frequency.first / 1000)}K"
                else frequency.first.toString()
                if (label.isNotEmpty()) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = label,
                        color = AppRes.colors.secondary,
                        fontSize = 12.sp,
                        style = AppRes.type.gilroyBold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun EqualizerPreview() {
    EqualizerTheme(colorUiType = ColorUiType.DARK) {
        Equalizer(
            modifier = Modifier
                .height(400.dp)
                .wrapContentWidth(),
            valueRange = -10f..10f,
            frequencies = listOf(
                32 to 10f,
                64 to 9f,
                128 to 8f,
                256 to 0f,
                512 to -1f,
                1024 to -2f,
                2048 to -3f,
                4096 to -4f,
                8192 to -5f,
            ),
            onGainChanged = { _, _ -> }
        )
    }
}