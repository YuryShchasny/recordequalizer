package com.sb.recordequalizer.features.home.ui.composable

import android.media.AudioDeviceInfo
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
import androidx.compose.ui.unit.sp
import com.sb.core.resources.AppRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioDeviceDropDownMenu(
    modifier: Modifier = Modifier,
    label: String,
    list: List<AudioDeviceInfo>,
    onSelected: (AudioDeviceInfo) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<AudioDeviceInfo?>(null) }
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            value = selected?.let { device ->
                device.productName.toString() + "\n" + getAudioDeviceTypeString(
                    device.type
                )
            } ?: "Default",
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = label,
                    color = AppRes.colors.secondary,
                    fontSize = 10.sp,
                    style = AppRes.type.gilroy
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            list.forEach { device ->
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
                        selected = device
                        expanded = false
                        onSelected(device)
                    },
                )
            }
        }
    }
}

private fun getAudioDeviceTypeString(audioDeviceType: Int): String {
    return when (audioDeviceType) {
        AudioDeviceInfo.TYPE_BUILTIN_MIC -> "Микрофон"
        AudioDeviceInfo.TYPE_BLUETOOTH_SCO -> "Bluetooth устройство"
        AudioDeviceInfo.TYPE_TELEPHONY -> "Телефонный приемник"
        AudioDeviceInfo.TYPE_REMOTE_SUBMIX -> "Удаленный субмикс"
        AudioDeviceInfo.TYPE_BUILTIN_EARPIECE -> "Динамик"
        AudioDeviceInfo.TYPE_BUILTIN_SPEAKER -> "Система динамиков"
        AudioDeviceInfo.TYPE_BLUETOOTH_A2DP -> "Bluetooth A2DP устройство"
        else -> "Неизвестный тип"
    }
}