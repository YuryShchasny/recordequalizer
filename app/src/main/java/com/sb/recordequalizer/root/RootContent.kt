package com.sb.recordequalizer.root

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
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sb.core.composable.Launched
import com.sb.core.resources.AppRes
import com.sb.core.resources.theme.EqualizerTheme
import com.sb.recordequalizer.features.home.ui.HomeContent
import com.sb.recordequalizer.root.component.RootComponent
import com.sb.recordequalizer.root.component.RootStore

@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier,
    keepSplashScreen: (Boolean) -> Unit = {},
) {
    val state by component.rootStore.state.subscribeAsState()
    Launched {
        component.rootStore.init()
    }
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
                    Children(
                        stack = component.stack,
                        modifier = Modifier.fillMaxSize(),
                        animation = stackAnimation()
                    ) {
                        when (val instance = it.instance) {
                            is RootComponent.Child.Home -> HomeContent(component = instance.component)
                        }
                    }
                }
            }
        }

        RootStore.State.Progress -> {
            keepSplashScreen(true)
        }
    }
}
