package com.sb.recordequalizer.features.home.ui

import android.Manifest
import android.media.AudioDeviceInfo
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sb.core.composable.Loading
import com.sb.core.resources.AppRes
import com.sb.recordequalizer.features.home.component.HomeComponent
import com.sb.recordequalizer.features.home.component.HomeStore
import com.sb.recordequalizer.features.home.ui.composable.AudioDeviceDropDownMenu
import com.sb.recordequalizer.features.home.ui.composable.ChannelCheckboxes
import com.sb.recordequalizer.features.home.ui.composable.Equalizer
import com.sb.recordequalizer.features.home.ui.composable.RequestPermissionContent

@Composable
fun HomeContent(
    component: HomeComponent,
    modifier: Modifier = Modifier,
) {
    val permissionRequestLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                component.homeStore.dispatchIntent(HomeStore.Intent.PermissionsGranted)
            } else {
                component.homeStore.dispatchIntent(HomeStore.Intent.PermissionsDenied)
            }
        }
    val state by component.homeStore.state.collectAsState()
    if (state.loading) {
        Loading()
        LaunchedEffect(permissionRequestLauncher) {
            permissionRequestLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    } else {
        if (state.hasPermissions) {
            HomeScreenContent(
                modifier = modifier.fillMaxSize(),
                frequencies = state.frequencies,
                isPlaying = state.playing,
                onPlayClick = { component.homeStore.dispatchIntent(HomeStore.Intent.PlayPause) },
                inputDevices = state.inputDevices,
                outputDevices = state.outputDevices,
                onSelectInputDevice = {
                    component.homeStore.dispatchIntent(
                        HomeStore.Intent.SelectInputDevice(
                            it
                        )
                    )
                },
                onSelectOutputDevice = {
                    component.homeStore.dispatchIntent(
                        HomeStore.Intent.SelectOutputDevice(
                            it
                        )
                    )
                },
                onLeftChannelChanged = {
                    component.homeStore.dispatchIntent(
                        HomeStore.Intent.ChangeLeftChannel(
                            it
                        )
                    )
                },
                onRightChannelChanged = {
                    component.homeStore.dispatchIntent(
                        HomeStore.Intent.ChangeRightChannel(
                            it
                        )
                    )
                },
                onFrequencyGainChanged = { frequency, value ->
                    component.homeStore.dispatchIntent(
                        HomeStore.Intent.FrequencyGainChanged(
                            frequency, value
                        )
                    )
                }
            )
        } else {
            RequestPermissionContent(permissionRequestLauncher = permissionRequestLauncher)
        }
    }
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    frequencies: List<Pair<Int, Float>>,
    isPlaying: Boolean,
    inputDevices: List<AudioDeviceInfo>,
    outputDevices: List<AudioDeviceInfo>,
    onSelectInputDevice: (AudioDeviceInfo) -> Unit,
    onSelectOutputDevice: (AudioDeviceInfo) -> Unit,
    onLeftChannelChanged: (Boolean) -> Unit,
    onRightChannelChanged: (Boolean) -> Unit,
    onPlayClick: () -> Unit,
    onFrequencyGainChanged: (Int, Float) -> Unit,
) {
    Box(modifier = modifier.padding(vertical = 32.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AudioDeviceDropDownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    list = inputDevices,
                    label = AppRes.strings.recordDevice,
                    onSelected = onSelectInputDevice
                )
                AudioDeviceDropDownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    list = outputDevices,
                    label = AppRes.strings.playbackDevice,
                    onSelected = onSelectOutputDevice
                )
            }
            Equalizer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                frequencies = frequencies,
                valueRange = -10f..10f,
                onGainChanged = onFrequencyGainChanged
            )
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(32.dp)
                    .clickable { onPlayClick() },
                imageVector = if (isPlaying) AppRes.icons.pause else AppRes.icons.play,
                contentDescription = null,
                tint = AppRes.colors.primary
            )
            if (isPlaying) {
                ChannelCheckboxes(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onLeftChannelChanged = onLeftChannelChanged,
                    onRightChannelChanged = onRightChannelChanged
                )
            }
        }
    }
}
