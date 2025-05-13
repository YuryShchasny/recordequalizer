package com.sb.core.composable

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@Composable
fun <T> MessagesHandler(
    messageFlow: SharedFlow<T>,
    messageStringProvider: @Composable (T?) -> String?
) {
    var message by remember { mutableStateOf<T?>(null) }
    val scope = rememberCoroutineScope()
    message?.let {
        Toast.makeText(LocalContext.current, messageStringProvider(it), Toast.LENGTH_LONG).show()
        message = null
    }
    LaunchedEffect(Unit) {
        scope.launch {
            messageFlow.collect {
                message = it
            }
        }
    }
}