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
fun <T> ErrorHandler(
    errorFlow: SharedFlow<T>,
    errorMessageProvider: @Composable (T?) -> String?
) {
    var error by remember { mutableStateOf<T?>(null) }
    val scope = rememberCoroutineScope()
    error?.let {
        Toast.makeText(LocalContext.current, errorMessageProvider(it), Toast.LENGTH_SHORT).show()
        error = null
    }
    LaunchedEffect(Unit) {
        scope.launch {
            errorFlow.collect {
                error = it
            }
        }
    }
}