package com.sb.home.presentation.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sb.core.composable.ClickableIcon
import com.sb.core.composable.ErrorHandler
import com.sb.core.composable.Launched
import com.sb.core.composable.Loading
import com.sb.core.resources.AppRes
import com.sb.core.resources.theme.ColorUiType
import com.sb.core.resources.theme.EqualizerTheme
import com.sb.home.presentation.component.HomeComponent
import com.sb.home.presentation.component.HomeStore
import com.sb.home.presentation.ui.composable.AudioDeviceDropDownMenu
import com.sb.home.presentation.ui.composable.ListenButton
import com.sb.home.presentation.ui.composable.RecordButton
import com.sb.home.presentation.ui.composable.Waveform

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
            dispatchIntent = component.homeStore::dispatchIntent,
            onEqualizerIconClick = component::onEqualizerClick,
            onChangeThemeClick = component::onChangeThemeClick
        )
        Launched {
            component.homeStore.initListener()
        }
    } ?: Loading()
    ErrorHandler(
        errorFlow = component.homeStore.error,
        errorMessageProvider = {
            when (it) {
                HomeStore.Error.SelectDeviceError -> AppRes.strings.errorSelectDevice
                null -> null
            }
        }
    )
}

@Composable
private fun HomeScreenContent(
    state: HomeStore.State,
    dispatchIntent: (HomeStore.Intent) -> Unit,
    onEqualizerIconClick: () -> Unit,
    onChangeThemeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.padding(vertical = 16.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnimatedContent(
                    targetState = AppRes.theme == ColorUiType.DARK,
                    transitionSpec = {
                        (slideInVertically(initialOffsetY = { -it }) + fadeIn()).togetherWith(
                            slideOutVertically(targetOffsetY = { it }) + fadeOut()
                        )
                    },
                ) { target ->
                    ClickableIcon(
                        modifier = Modifier.size(24.dp),
                        imageVector = if (target) AppRes.icons.moon else AppRes.icons.sun,
                        tint = AppRes.colors.secondary,
                        rippleRadius = 20.dp,
                        onClick = onChangeThemeClick
                    )
                }
                ClickableIcon(
                    modifier = Modifier.size(24.dp),
                    imageVector = AppRes.icons.slidersUp,
                    tint = AppRes.colors.secondary,
                    rippleRadius = 20.dp,
                    onClick = onEqualizerIconClick
                )
            }
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AudioDeviceDropDownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    selectedDevice = state.selectedInputDevice,
                    devices = state.inputDevices,
                    label = AppRes.strings.recordDevice,
                    onSelected = { dispatchIntent(HomeStore.Intent.SelectInputDevice(it)) }
                )
                AudioDeviceDropDownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    selectedDevice = state.selectedOutputDevice,
                    devices = state.outputDevices,
                    label = AppRes.strings.playbackDevice,
                    onSelected = { dispatchIntent(HomeStore.Intent.SelectOutputDevice(it)) }
                )
            }
            Waveform(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                amplitudes = state.streamAmplitudes,
                scale = 12f
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                AnimatedContent(
                    targetState = state.playing
                ) { isPlaying ->
                    if (isPlaying) {
                        if (state.recordMode) {
                            RecordButton(
                                isPlaying = true,
                                onClick = { dispatchIntent(HomeStore.Intent.RecordClick) }
                            )
                        } else {
                            ListenButton(
                                isPlaying = true,
                                onClick = { dispatchIntent(HomeStore.Intent.ListenClick) }
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                        ) {
                            ListenButton(
                                isPlaying = false,
                                onClick = { dispatchIntent(HomeStore.Intent.ListenClick) }
                            )
                            RecordButton(
                                isPlaying = false,
                                onClick = { dispatchIntent(HomeStore.Intent.RecordClick) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    EqualizerTheme(colorUiType = ColorUiType.DARK) {
        HomeScreenContent(
            state = HomeStore.State(
                playing = true,
                recordMode = true
            ),
            dispatchIntent = {},
            onEqualizerIconClick = {},
            onChangeThemeClick = {}
        )
    }
}