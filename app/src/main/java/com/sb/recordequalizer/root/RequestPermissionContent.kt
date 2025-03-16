package com.sb.recordequalizer.root

import android.Manifest
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sb.core.resources.AppRes

@Composable
fun RequestPermissionContent(
    modifier: Modifier = Modifier,
    permissionRequestLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            text = AppRes.strings.requestPermission,
            color = AppRes.colors.primary,
            fontSize = 20.sp,
            style = AppRes.type.gilroyBold,
            textAlign = TextAlign.Center
        )
    }
    LaunchedEffect(permissionRequestLauncher) {
        permissionRequestLauncher.launch(
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        )
    }
}