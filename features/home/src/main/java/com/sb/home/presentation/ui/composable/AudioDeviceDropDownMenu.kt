package com.sb.home.presentation.ui.composable

import android.media.AudioDeviceInfo
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sb.core.resources.AppRes
import com.sb.core.resources.theme.ColorUiType
import com.sb.core.resources.theme.EqualizerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioDeviceDropDownMenu(
    modifier: Modifier = Modifier,
    label: String,
    selectedDevice: AudioDeviceInfo?,
    devices: List<AudioDeviceInfo>,
    onSelected: (AudioDeviceInfo) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            modifier = modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            value = selectedDevice?.let { device ->
                device.productName.toString() + " " + getAudioDeviceTypeString(
                    device.type
                )
            } ?: AppRes.strings.default,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = {
                Text(
                    text = label,
                    color = AppRes.colors.secondary,
                    fontSize = 10.sp,
                    style = AppRes.type.gilroy
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(12.dp),
            colors = ExposedDropdownMenuDefaults.textFieldColors().copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = AppRes.colors.secondary.copy(alpha = 0.1f),
                unfocusedContainerColor = AppRes.colors.secondary.copy(alpha = 0.1f),
            )
        )
        if (devices.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                shape = RoundedCornerShape(12.dp),
            ) {
                devices.forEach { device ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = device.productName.toString() + "\n" + getAudioDeviceTypeString(
                                    device.type
                                ),
                                color = AppRes.colors.primary,
                                fontSize = 14.sp,
                                style = AppRes.type.gilroyMedium
                            )
                        },
                        onClick = {
                            expanded = false
                            onSelected(device)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun getAudioDeviceTypeString(audioDeviceType: Int): String {
    return when (audioDeviceType) {
        AudioDeviceInfo.TYPE_BUILTIN_MIC -> AppRes.strings.mic
        AudioDeviceInfo.TYPE_BLUETOOTH_SCO -> AppRes.strings.bluetoothSco
        AudioDeviceInfo.TYPE_TELEPHONY -> AppRes.strings.telephony
        AudioDeviceInfo.TYPE_REMOTE_SUBMIX -> AppRes.strings.remoteSubmix
        AudioDeviceInfo.TYPE_BUILTIN_EARPIECE -> AppRes.strings.builtinEarpiece
        AudioDeviceInfo.TYPE_BUILTIN_SPEAKER -> AppRes.strings.builtinSpeaker
        AudioDeviceInfo.TYPE_BLUETOOTH_A2DP -> AppRes.strings.bluetoothA2dp
        else -> AppRes.strings.unknown
    }
}

@Preview
@Composable
private fun AudioDeviceDropDownMenuPreview() {
    EqualizerTheme(colorUiType = ColorUiType.DARK) {
        AudioDeviceDropDownMenu(
            label = "Устройство вывода",
            devices = listOf(),
            selectedDevice = null,
            onSelected = {}
        )
    }
}
