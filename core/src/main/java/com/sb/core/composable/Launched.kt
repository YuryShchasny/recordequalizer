package com.sb.core.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope

@Composable
fun Launched(block: suspend CoroutineScope.() -> Unit) {
    LaunchedEffect(Unit, block)
}