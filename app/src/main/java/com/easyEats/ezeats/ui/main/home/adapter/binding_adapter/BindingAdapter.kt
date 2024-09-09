package com.easyEats.ezeats.ui.main.home.adapter.binding_adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.response.home_response.FeaturedOffer
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.easyEats.ezeats.ui.main.home.adapter.featured.FeaturedOfferAdapter
import com.easyEats.ezeats.ui.main.home.adapter.hot_deals.HotDealsAdapter
import com.todkars.shimmer.ShimmerRecyclerView
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
@BindingAdapter("showShimmer")
fun showShimmer(shimmerRecyclerView: ShimmerRecyclerView, showShimmer: Boolean) {
    if (showShimmer) {
        shimmerRecyclerView.showShimmer() // Show the shimmer effect.
    } else {
        shimmerRecyclerView.hideShimmer() // Hide the shimmer effect.
    }
}



