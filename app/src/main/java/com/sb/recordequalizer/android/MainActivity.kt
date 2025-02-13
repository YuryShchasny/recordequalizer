package com.sb.recordequalizer.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arkivanov.decompose.defaultComponentContext
import com.sb.recordequalizer.root.RootContent
import com.sb.recordequalizer.root.component.DefaultRootComponent

class MainActivity : ComponentActivity() {

    private val rootComponent by lazy {
        DefaultRootComponent(
            componentContext = defaultComponentContext()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()
        setContent {
            RootContent(
                component = rootComponent,
                keepSplashScreen = { splashScreen.setKeepOnScreenCondition { it } },
            )
        }
    }
}