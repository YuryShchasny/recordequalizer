package com.sb.domain.entity

data class Settings(
    val selectedProfileId: Long = -1,
    val selectedInputDevice: Int = -1,
    val selectedOutputDevice: Int = -1,
    val theme: Theme = Theme.DARK
) {
    enum class Theme {
        DARK,
        LIGHT
    }
}