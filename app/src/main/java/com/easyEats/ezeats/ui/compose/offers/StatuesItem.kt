package com.easyEats.ezeats.ui.compose.offers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daman.edman.screens.components.IconTextView
import com.easyEats.ezeats.R
import com.easyEats.ezeats.ui.GreenColor
import com.easyEats.ezeats.ui.OrangeColor
import com.easyEats.ezeats.ui.RedColor

@Composable
fun StatuesItem(
    modifier: Modifier = Modifier,
    hasFreeDelivery : Boolean = false,
    hasOffers: Boolean = false,
    hasDiscount: Boolean = false,
    percentage: String = "",
    delivery: String = "",
    offers: String = "Offers",
) {

    Surface(
        modifier = modifier
            .height(22.dp),
        shape = RoundedCornerShape(5.dp),
        color = when {
            hasDiscount -> GreenColor.copy(alpha = 0.2f)
            hasFreeDelivery -> RedColor.copy(alpha = 0.2f)
            hasOffers -> OrangeColor.copy(alpha = 0.2f)
            else -> Color.Red.copy(alpha = 0.2f)
        }
    ) {
        IconTextView(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = when {
                hasDiscount -> "$percentage% off"
                hasFreeDelivery -> delivery
                hasOffers -> offers
                else -> "Unknown"
            },
            icon = when {
                hasDiscount -> R.drawable.ic_gift
                hasFreeDelivery -> R.drawable.ic_delivery
                hasOffers -> R.drawable.ic_offers
                else -> R.drawable.close_ic
            },
            textColor = when {
                hasDiscount -> GreenColor
                hasFreeDelivery -> RedColor
                hasOffers -> OrangeColor
                else -> Color.Red
            },
            tint = when {
                hasDiscount -> GreenColor
                hasFreeDelivery -> RedColor
                hasOffers -> OrangeColor
                else -> Color.Red
            },
        )
    }
}
@Preview(showBackground = true)
@Composable
private fun StatuesItemPreview() {

    Column {
        StatuesItem(
            modifier = Modifier,
            percentage = "20",
            hasFreeDelivery = true,
            delivery = "Free Delivery"
        )

        StatuesItem(
            modifier = Modifier,
            percentage = "20",
            hasOffers = true,
            delivery = "Free Delivery"
        )

        StatuesItem(
            modifier = Modifier,
            percentage = "20",
            hasDiscount = true,
            delivery = "Free Delivery"
        )
    }

}