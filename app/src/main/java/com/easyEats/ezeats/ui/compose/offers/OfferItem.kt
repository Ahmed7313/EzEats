package com.easyEats.ezeats.ui.compose.offers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.daman.edman.screens.components.AppSpacer
import com.daman.edman.screens.components.HeaderText
import com.daman.edman.screens.components.IconTextView
import com.daman.edman.screens.components.NormalText
import com.easyEats.ezeats.R
import com.easyEats.ezeats.ui.extraMedium
import com.google.android.play.core.ktx.AppUpdateResult

@Composable
fun OfferItem(
    resturantName : String,
    resturantType : String,
    distance: String,
    resturantImage : String,
    available : Boolean = false,
    modifier: Modifier = Modifier,
    hasDiscount : Boolean = false,
    discountPercentage: String,
    hasFreeDelivery : Boolean = false,
    hasOffers : Boolean = false,
    onClick: () -> Unit
) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(105.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(5.dp),
        color = Color.White,
        shadowElevation = 4.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .size(105.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(resturantImage),
                    contentDescription = "Offer Background",
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        // dim the image when closed
                        .alpha(if (!available) 0.5f else 1f)
                )

                if (!available) {
                    // optional: a dark overlay
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.8f))
                    )
                    HeaderText(
                        text = "Closed",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            AppSpacer(width = extraMedium)

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2f)
                    .padding(top = 4.dp, bottom = 10.dp)
            ) {

                HeaderText(text = resturantName)

                AppSpacer(height = 4.dp)

                NormalText(text = resturantType)

                AppSpacer(height = 4.dp)

                IconTextView(icon = R.drawable.marker_pin_icon, text = distance)

                AppSpacer(height = 16.dp)

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(extraMedium),
                    // you can remember a scroll state if you want to observe scroll position:
                    // state = rememberLazyListState()
                ) {
                    if (hasDiscount) {
                        item {
                            StatuesItem(
                                hasDiscount = true,
                                percentage = discountPercentage
                            )
                        }
                    }
                    if (hasFreeDelivery) {
                        item {
                            StatuesItem(
                                hasFreeDelivery = true,
                                delivery = "Free Delivery"
                            )
                        }
                    }
                    if (hasOffers) {
                        item {
                            StatuesItem(
                                hasOffers = true,
                                offers = "Offers"
                            )
                        }
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun OfferItemPreview() {

    Column {
        OfferItem(
            resturantName = "Resturant Name",
            resturantType = "Resturant Type",
            distance = "2.5 km",
            resturantImage = "https://example.com/image.jpg",
            available = false,
            hasDiscount = true,
            discountPercentage = "20%",
            hasFreeDelivery = true,
            hasOffers = true,
            onClick = {}
        )

        AppSpacer(height = 16.dp)

        OfferItem(
            resturantName = "Closed Resturant",
            resturantType = "Resturant Type",
            distance = "2.5 km",
            resturantImage = "https://example.com/image.jpg",
            available = true,
            hasDiscount = false,
            discountPercentage = "",
            hasFreeDelivery = false,
            hasOffers = false,
            onClick = {}
        )
    }

}