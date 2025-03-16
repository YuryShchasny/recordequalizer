package com.sb.recordequalizer.root

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sb.core.composable.Launched
import com.sb.core.resources.AppRes
import com.sb.core.resources.theme.EqualizerTheme
import com.sb.equalizer.presentation.ui.EqualizerContent
import com.sb.home.presentation.ui.HomeContent
import com.sb.recordequalizer.root.component.RootComponent
import com.sb.recordequalizer.root.component.RootStore

@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier,
    keepSplashScreen: (Boolean) -> Unit = {},
) {
    val permissionRequestLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.all { permission -> permission.value }) {
                component.rootStore.dispatchIntent(RootStore.Intent.PermissionsGranted)
            } else {
                component.rootStore.dispatchIntent(RootStore.Intent.PermissionsDenied)
            }
        }
    val state by component.rootStore.state.subscribeAsState()
    when (state) {
        is RootStore.State.Ready -> {
            keepSplashScreen(false)
            EqualizerTheme(
                language = (state as RootStore.State.Ready).language,
                colorUiType = (state as RootStore.State.Ready).colorUiType
            ) {
                val systemUiController = rememberSystemUiController()
                systemUiController.setStatusBarColor(AppRes.colors.background)
                systemUiController.setNavigationBarColor(AppRes.colors.background)
                Surface(
                    modifier = modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars)
                        .windowInsetsPadding(WindowInsets.ime),
                    color = AppRes.colors.background
                ) {
                    if ((state as RootStore.State.Ready).hasPermissions) {
                        Children(
                            stack = component.stack,
                            modifier = Modifier.fillMaxSize(),
                            animation = stackAnimation(
                                animator = slide(orientation = Orientation.Horizontal)
                            )
                        ) {
                            when (val instance = it.instance) {
                                is RootComponent.Child.Home -> HomeContent(component = instance.component)
                                is RootComponent.Child.Equalizer -> EqualizerContent(component = instance.component)
                            }
                        }
                    } else RequestPermissionContent(permissionRequestLauncher = permissionRequestLauncher)
                }
            }
        }

        RootStore.State.Progress -> {
            keepSplashScreen(true)
            Launched {
                permissionRequestLauncher.launch(
                    arrayOf(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                    )
                )
            }
        }
    }
}
