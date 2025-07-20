package com.easyEats.ezeats.ui.compose.cart.paymentMethod

import androidx.compose.ui.graphics.painter.Painter

data class PaymentMethodModel(
    val text: String = "",
    val paymentMethod: PAYMENT_METHOD? = null,
    val painter: Painter? = null,
    val isVector: Boolean = true,
    val key: String = "",
)