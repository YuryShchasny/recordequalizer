package com.sb.home.presentation.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sb.core.R

@Composable
fun ListenButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeButton(
        modifier = modifier,
        text = if (isPlaying) stringResource(R.string.stop_listening)
        else stringResource(R.string.start_listening),
        onClick = onClick
    )
}