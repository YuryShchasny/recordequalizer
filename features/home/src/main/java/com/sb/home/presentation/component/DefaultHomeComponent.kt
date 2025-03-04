package com.sb.home.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate

class DefaultHomeComponent(
    componentContext: ComponentContext,
    private val openEqualizer: () -> Unit,
    private val changeTheme: () -> Unit,
) : HomeComponent, ComponentContext by componentContext {
    override val homeStore: HomeStore = instanceKeeper.getOrCreate {
        HomeStore()
    }

    override fun onEqualizerClick() = openEqualizer()
    override fun onChangeThemeClick() = changeTheme()
}
