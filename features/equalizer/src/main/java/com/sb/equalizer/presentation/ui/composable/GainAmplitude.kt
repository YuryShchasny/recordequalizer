package com.sb.equalizer.presentation.ui.composable

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sb.core.resources.AppRes
import com.sb.core.resources.theme.ColorUiType
import com.sb.core.resources.theme.EqualizerTheme

@Composable
fun GainAmplitude(
    value: Float,
    onValueChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = AppRes.strings.gainAmplitude,
            color = AppRes.colors.primary,
            fontSize = 12.sp,
            style = AppRes.type.gilroySemibold
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "-10dB",
                color = AppRes.colors.secondary,
                fontSize = 8.sp,
                style = AppRes.type.gilroyMedium
            )
            CustomSlider(
                modifier = Modifier.weight(1f),
                value = value,
                onValueChanged = onValueChanged,
                valueRange = -10f..10f,
                orientation = Orientation.Horizontal,
            )
            Text(
                text = "+10dB",
                color = AppRes.colors.secondary,
                fontSize = 8.sp,
                style = AppRes.type.gilroyMedium
            )
        }
    }
}

@Preview
@Composable
private fun GainAmplitudePreview() {
    EqualizerTheme(colorUiType = ColorUiType.DARK) {
        GainAmplitude(
            value = 0f,
            onValueChanged = {}
        )
    }
}