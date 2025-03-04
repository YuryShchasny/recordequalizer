package com.sb.recordequalizer.root.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.sb.equalizer.presentation.component.EqualizerComponent
import com.sb.home.presentation.component.HomeComponent

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    val rootStore: RootStore

    sealed class Child {
        class Home(val component: HomeComponent) : Child()
        class Equalizer(val component: EqualizerComponent) : Child()
    }
}
