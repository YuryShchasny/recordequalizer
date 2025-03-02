package com.sb.equalizer.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sb.core.composable.Loading
import com.sb.equalizer.presentation.component.EqualizerComponent
import com.sb.equalizer.presentation.component.EqualizerStore
import com.sb.equalizer.presentation.ui.composable.ChannelCheckboxes
import com.sb.equalizer.presentation.ui.composable.Equalizer
import com.sb.equalizer.presentation.ui.composable.GainAmplitude

@Composable
fun EqualizerContent(
    component: EqualizerComponent,
    modifier: Modifier = Modifier,
) {

    val state by component.equalizerStore.state.collectAsState()
    state?.let {
        EqualizerScreenContent(
            modifier = modifier.fillMaxSize(),
            state = it,
            dispatchIntent = component.equalizerStore::dispatchIntent
        )
    } ?: Loading()
}

@Composable
private fun EqualizerScreenContent(
    state: EqualizerStore.State,
    dispatchIntent: (EqualizerStore.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.padding(vertical = 32.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Equalizer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                frequencies = state.frequencies,
                valueRange = -10f..10f,
                onGainChanged = { frequency, value ->
                    dispatchIntent(
                        EqualizerStore.Intent.FrequencyGainChanged(
                            frequency,
                            value
                        )
                    )
                }
            )
            GainAmplitude(
                modifier = Modifier.padding(horizontal = 16.dp),
                value = state.amplitude,
                onValueChanged = { dispatchIntent(EqualizerStore.Intent.AmplitudeGainChanged(it)) }
            )
            ChannelCheckboxes(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onLeftChannelChanged = { dispatchIntent(EqualizerStore.Intent.ChangeLeftChannel(it)) },
                onRightChannelChanged = { dispatchIntent(EqualizerStore.Intent.ChangeRightChannel(it)) }
            )
        }
    }
}
