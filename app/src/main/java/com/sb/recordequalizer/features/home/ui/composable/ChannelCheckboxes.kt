package com.sb.recordequalizer.features.home.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sb.core.resources.AppRes

@Composable
fun ChannelCheckboxes(
    modifier: Modifier = Modifier,
    onLeftChannelChanged: (Boolean) -> Unit,
    onRightChannelChanged: (Boolean) -> Unit,
) {
    var leftChannelState by remember { mutableStateOf(true) }
    var rightChannelState by remember { mutableStateOf(true) }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Checkbox(checked = leftChannelState, onCheckedChange = {
                leftChannelState = it
                onLeftChannelChanged(it)
            })
            Text(
                text = AppRes.strings.leftChannel,
                color = AppRes.colors.primary,
                fontSize = 16.sp,
                style = AppRes.type.gilroyMedium
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Checkbox(checked = rightChannelState, onCheckedChange = {
                rightChannelState = it
                onRightChannelChanged(it)
            })
            Text(
                text = AppRes.strings.rightChannel,
                color = AppRes.colors.primary,
                fontSize = 16.sp,
                style = AppRes.type.gilroyMedium
            )
        }
    }
}