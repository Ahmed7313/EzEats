package com.easyEats.ezeats.ui.compose.helper

object AppHelper {
    fun Double.formatTextNumber(): String {
        return this.toBigDecimal()
            .setScale(2, java.math.RoundingMode.HALF_UP)
            .toPlainString()
    }
}