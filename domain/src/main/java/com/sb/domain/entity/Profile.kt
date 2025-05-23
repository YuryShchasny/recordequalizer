package com.sb.domain.entity

data class Profile(
    val id: Long = 0,
    val name: String,
    val gains: List<Float>,
    val amplitude: Float,
    val leftChannel: Boolean,
    val rightChannel: Boolean,
    val compressorEnabled: Boolean,
) {
    fun hasDifference(
        amplitude: Float,
        gains: List<Float>,
        leftChannel: Boolean,
        rightChannel: Boolean,
        compressorEnabled: Boolean,
    ): Boolean {
        return this.gains != gains ||
                this.amplitude != amplitude ||
                this.leftChannel != leftChannel ||
                this.rightChannel != rightChannel ||
                this.compressorEnabled != compressorEnabled
    }
}
