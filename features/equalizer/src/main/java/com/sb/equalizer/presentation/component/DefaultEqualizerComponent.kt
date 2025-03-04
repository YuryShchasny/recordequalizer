package com.sb.equalizer.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate

class DefaultEqualizerComponent(
    componentContext: ComponentContext,
) : EqualizerComponent, ComponentContext by componentContext {
    override val equalizerStore: EqualizerStore = instanceKeeper.getOrCreate {
        EqualizerStore()
    }
}
