package com.easyEats.ezeats.ui.compose.helper

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object AppHelper {
    fun Double.formatTextNumber(): String {
        return this.toBigDecimal()
            .setScale(2, java.math.RoundingMode.HALF_UP)
            .toPlainString()
    }

    fun String.formatDateIso(): String {
        try {
            val date = LocalDate.parse(this.substring(0, 10)) // "2025-11-30"
            return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }catch (e: Exception) {
            e.printStackTrace()
            return this // Return the original string if parsing fails
        }
    }
}