package com.sb.features.home.presentation.ui

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sb.core.composable.Loading
import com.sb.core.resources.AppRes
import com.sb.features.home.presentation.component.HomeComponent
import com.sb.features.home.presentation.component.HomeStore
import com.sb.features.home.presentation.ui.composable.AudioDeviceDropDownMenu
import com.sb.features.home.presentation.ui.composable.ChannelCheckboxes
import com.sb.features.home.presentation.ui.composable.Equalizer
import com.sb.features.home.presentation.ui.composable.GainAmplitude

@Composable
fun HomeContent(
    component: HomeComponent,
    modifier: Modifier = Modifier,
) {

    val state by component.homeStore.state.collectAsState()
    state?.let {
        HomeScreenContent(
            modifier = modifier.fillMaxSize(),
            state = it,
            dispatchIntent = component.homeStore::dispatchIntent
        )
    } ?: Loading()
}

@Composable
private fun HomeScreenContent(
    state: HomeStore.State,
    dispatchIntent: (HomeStore.Intent) -> Unit,
    modifier: Modifier = Modifier,
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
                    list = state.inputDevices,
                    label = AppRes.strings.recordDevice,
                    onSelected = { dispatchIntent(HomeStore.Intent.SelectInputDevice(it)) }
                )
                AudioDeviceDropDownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    list = state.outputDevices,
                    label = AppRes.strings.playbackDevice,
                    onSelected = { dispatchIntent(HomeStore.Intent.SelectOutputDevice(it)) }
                )
            }
            Equalizer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                frequencies = state.frequencies,
                valueRange = -10f..10f,
                onGainChanged = { frequency, value ->
                    dispatchIntent(
                        HomeStore.Intent.FrequencyGainChanged(
                            frequency,
                            value
                        )
                    )
                }
            )
            GainAmplitude(
                modifier = Modifier.padding(horizontal = 16.dp),
                value = state.amplitude,
                onValueChanged = { dispatchIntent(HomeStore.Intent.AmplitudeGainChanged(it)) }
            )
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(32.dp)
                    .clickable { dispatchIntent(HomeStore.Intent.PlayPause) },
                imageVector = if (state.playing) AppRes.icons.pause else AppRes.icons.play,
                contentDescription = null,
                tint = AppRes.colors.primary
            )
            if (state.playing) {
                ChannelCheckboxes(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onLeftChannelChanged = { dispatchIntent(HomeStore.Intent.ChangeLeftChannel(it)) },
                    onRightChannelChanged = { dispatchIntent(HomeStore.Intent.ChangeRightChannel(it)) }
                )
            }
        }
    }
}
