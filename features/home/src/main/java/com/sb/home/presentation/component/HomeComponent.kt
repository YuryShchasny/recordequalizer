package com.sb.home.presentation.component

interface HomeComponent {
    val homeStore: HomeStore

    fun onEqualizerClick()
    fun onChangeThemeClick()
}
