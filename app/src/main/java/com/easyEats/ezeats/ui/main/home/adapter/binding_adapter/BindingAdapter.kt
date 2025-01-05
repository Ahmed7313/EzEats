package com.easyEats.ezeats.ui.main.home.adapter.binding_adapter

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.easyEats.ezeats.MyApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.response.home_response.ActiveOrder
import com.easyEats.ezeats.domain.main.model.response.home_response.FeaturedOffer
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.easyEats.ezeats.domain.main.model.response.partners.PartnersItem
import com.easyEats.ezeats.ui.main.home.adapter.featured.FeaturedOfferAdapter
import com.easyEats.ezeats.ui.main.home.adapter.hot_deals.HotDealsAdapter
import com.easyEats.ezeats.ui.main.home.adapter.partners.PartnersAdapter
//import com.todkars.shimmer.ShimmerRecyclerView
import me.relex.circleindicator.CircleIndicator3


/**
 * Binding adapter to load an image into an ImageView from a URL.
 *
 * @param imageView The ImageView to load the image into.
 * @param url The URL of the image to load. If null, no image will be loaded.
 */
@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView, url: String?) {
    imageView.load(url) {
        error(R.drawable.ez_logo) // Placeholder image to show in case of an error.
    }
}

@BindingAdapter("loadProfileImage")
fun loadProfileImage(imageView: ImageView, url: String?) {
    imageView.load(url) {
        error(R.drawable.dummy_image) // Placeholder image to show in case of an error.
    }
}

@BindingAdapter("setOurPartnersText")
fun setOurPartnersText(tv: TextView, listSize: Int) {
    val ourPartnersText =
        "With over $listSize partners of success, you are \nguaranteed to fulfill your cravings with EZeats"
    val spannable = SpannableString(ourPartnersText)

    // Define the part you want to color in red
    val startIndex = ourPartnersText.indexOf("$listSize partners of success")
    val endIndex = startIndex + "$listSize partners of success".length

    // Set the color span for the specified part
    spannable.setSpan(
        ForegroundColorSpan(MyApplication.baseApplication.getColor(R.color.main_color)), // Use Color.RED or your preferred color
        startIndex,
        endIndex,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    // Set the spannable text to the TextView
    tv.text = spannable
}

@BindingAdapter("setSpinWheelPartnersText")
fun setSpinWheelPartnersText(tv: TextView, listSize: Int) {
    val ourPartnersText =
        "Leave a review and spin the wheel, \nwith over $listSize partners , you are in it to win it! "
    val spannable = SpannableString(ourPartnersText)

    // Define the part you want to color in red
    val startIndex = ourPartnersText.indexOf("$listSize partners ")
    val endIndex = startIndex + "$listSize partners".length

    // Set the color span for the specified part
    spannable.setSpan(
        ForegroundColorSpan(MyApplication.baseApplication.getColor(R.color.main_color)), // Use Color.RED or your preferred color
        startIndex,
        endIndex,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    // Set the spannable text to the TextView
    tv.text = spannable
}

/**
 * Binding adapter to set data to a RecyclerView using the FeaturedOfferAdapter.
 *
 * @param rv The RecyclerView to bind the data to.
 * @param featuredOfferAdapter The adapter to use for displaying the featured offers.
 * @param list The list of FeaturedOffer items to display. If null, an empty list is used.
 */
@BindingAdapter("featuredAdapter", "data")
fun setFeaturesDataToRecyclerView(
    rv: RecyclerView,
    featuredOfferAdapter: FeaturedOfferAdapter,
    list: List<FeaturedOffer>?
) {
    rv.adapter = featuredOfferAdapter
    featuredOfferAdapter.submitList(list ?: emptyList())
}

/**
 * Binding adapter to set data to a RecyclerView using the HotDealsAdapter.
 *
 * @param rv The RecyclerView to bind the data to.
 * @param hotDealsAdapter The adapter to use for displaying the hot deals.
 * @param list The list of HotDeal items to display. If null, an empty list is used.
 */
@BindingAdapter("hotDealsAdapter", "data")
fun setHotDealsDataToRecyclerView(
    rv: RecyclerView,
    hotDealsAdapter: HotDealsAdapter,
    list: List<HotDeal>?
) {
    rv.adapter = hotDealsAdapter
    hotDealsAdapter.submitList(list ?: emptyList())
}


@BindingAdapter("partnersAdapter", "partners")
fun setPartnersDataToRecyclerView(
    rv: RecyclerView,
    hotDealsAdapter: PartnersAdapter,
    list: List<PartnersItem>?
) {
    list?.let {
        // Create a new list to hold the duplicated items
        val duplicatedList = mutableListOf<PartnersItem>()

        // Repeat the list multiple times
        repeat(20) { duplicatedList.addAll(
            list
        ) } // Add the whole list each time

        // Set the adapter to the RecyclerView
        rv.adapter = hotDealsAdapter

        // Submit the duplicated list to the adapter
        hotDealsAdapter.submitList(duplicatedList)
    }
}

/**
 * Binding adapter to bind a CircleIndicator3 with a RecyclerView to synchronize page indicators with scroll position.
 *
 * @param indicator The CircleIndicator3 to display the page indicators.
 * @param rvFeatured The RecyclerView that displays the featured offers.
 * @param featuredOffers The list of featured offers used to determine the number of indicators.
 */
@BindingAdapter("indicatorRecyclerView", "data")
fun bindCircleIndicator(
    indicator: CircleIndicator3,
    rvFeatured: RecyclerView,
    featuredOffers: List<FeaturedOffer>?
) {
    val size = featuredOffers?.size ?: 0
    indicator.createIndicators(size, 0) // Initialize the indicators.

    rvFeatured.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
            val firstVisibleItemPosition = layoutManager?.findFirstVisibleItemPosition() ?: 0
            indicator.animatePageSelected(firstVisibleItemPosition) // Update the selected indicator based on scroll position.
        }
    })
}

/**
 * Binding adapter to show or hide a ShimmerRecyclerView based on a boolean flag.
 *
 * @param shimmerRecyclerView The ShimmerRecyclerView to control the shimmer effect.
 * @param showShimmer A boolean flag indicating whether to show or hide the shimmer effect.
 */
//@BindingAdapter("showShimmer")
//fun showShimmer(shimmerRecyclerView: ShimmerRecyclerView, showShimmer: Boolean) {
//    if (showShimmer) {
//        shimmerRecyclerView.showShimmer() // Show the shimmer effect.
//    } else {
//        shimmerRecyclerView.hideShimmer() // Hide the shimmer effect.
//    }
//}


@BindingAdapter("setTitleOfActiveOrder")
fun setTitleOfActiveOrder(tv: TextView, activeOrder: ActiveOrder?) {
    val text = "Your Order is"
    val state = activeOrder?.displayState
    val fullText = "$text $state"
    val spannable = SpannableString(fullText)

    spannable.setSpan(
        ForegroundColorSpan(MyApplication.baseApplication.getColor(R.color.main_color)),
        14, fullText.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    tv.text = spannable
}




