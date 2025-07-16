package com.easyEats.ezeats.ui.compose.cart.paymentSummaryScreen.dummy

data class TipOption(
    val id: String,
    var amount: String?, // Null for custom tip
    var percentage: String?, // Null for custom tip
    val isCustom: Boolean = false
)

