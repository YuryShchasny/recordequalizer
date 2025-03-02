package com.sb.recordequalizer.root.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.sb.equalizer.presentation.component.DefaultEqualizerComponent
import com.sb.equalizer.presentation.component.EqualizerComponent
import com.sb.home.presentation.component.DefaultHomeComponent
import com.sb.home.presentation.component.HomeComponent
import com.sb.recordequalizer.root.component.RootComponent.Child
import kotlinx.serialization.Serializable

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    override val rootStore = instanceKeeper.getOrCreate {
        RootStore()
    }

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Home,
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(config: Config, childComponentContext: ComponentContext): Child =
        when (config) {
            is Config.Home -> Child.Home(homeComponent(childComponentContext))
            Config.Equalizer -> Child.Equalizer(equalizerComponent(childComponentContext))
        }

    private fun homeComponent(componentContext: ComponentContext): HomeComponent =
        DefaultHomeComponent(
            componentContext = componentContext,
            openEqualizer = {
                navigation.pushToFront(Config.Equalizer)
            },
            changeTheme = {
                rootStore.dispatchIntent(RootStore.Intent.ChangeTheme)
            }
        )

    private fun equalizerComponent(componentContext: ComponentContext): EqualizerComponent =
        DefaultEqualizerComponent(
            componentContext = componentContext,
        )

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Home : Config

        @Serializable
        data object Equalizer : Config
    }
}
