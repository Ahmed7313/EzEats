package com.easyEats.ezeats.ui.compose.cart.paymentSummaryScreen.dummy

data class TipOption(
    val id: String,
    val amount: String?, // Null for custom tip
    val percentage: String?, // Null for custom tip
    val isCustom: Boolean = false
)

