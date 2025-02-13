package com.sb.recordequalizer.features.home.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate

class DefaultHomeComponent(
    componentContext: ComponentContext,
) : HomeComponent, ComponentContext by componentContext {
    override val homeStore: HomeStore = instanceKeeper.getOrCreate {
        HomeStore(lifecycle)
    }
}
