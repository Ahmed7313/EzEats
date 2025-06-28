package com.easyEats.ezeats.ui.compose.offers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.new_restaurants_reponse.RestaurantsResponseItem

@Composable
fun OffersList(
    restaurants: List<RestaurantsResponseItem>,
    onRestaurantClick: (RestaurantsResponseItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
    ) {
        items(restaurants) { restaurant ->
            OfferItem(
                restaurant = restaurant,
                resturantName = restaurant.name ?: "",
                resturantType = restaurant.locationName ?: "",
                distance = "${restaurant.distance ?: 0} km",
                resturantImage = restaurant.logo ?: "",
                available = restaurant.available ?: false,
                hasDiscount = restaurant.hasDiscount ?: false,
                discountPercentage = "${restaurant.discountOnMenu ?: 0}%",
                hasFreeDelivery = restaurant.hasFreeDelivery ?: false,
                hasOffers = restaurant.hasOffers ?: false,
                onClick = { onRestaurantClick(restaurant) }
            )
        }
    }
} 