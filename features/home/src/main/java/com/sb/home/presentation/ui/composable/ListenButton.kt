package com.sb.home.presentation.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sb.core.resources.AppRes

@Composable
fun ListenButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeButton(
        modifier = modifier,
        text = if (isPlaying) AppRes.strings.stopListening
        else AppRes.strings.startListening,
        onClick = onClick
    )
}