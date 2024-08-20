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

@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView, url: String?) {
    imageView.load(url) {
        error(R.drawable.ez_logo)
    }
}


@BindingAdapter("featuredAdapter", "data")
fun setFeaturesDataToRecyclerView(
    rv: RecyclerView,
    featuredOfferAdapter: FeaturedOfferAdapter,
    list: List<FeaturedOffer>?
) {
    rv.adapter = featuredOfferAdapter
    featuredOfferAdapter.submitList(list ?: emptyList())
}

@BindingAdapter("hotDealsAdapter", "data")
fun setHotDealsDataToRecyclerView(
    rv: RecyclerView,
    hotDealsAdapter: HotDealsAdapter,
    list: List<HotDeal>?
) {
    rv.adapter = hotDealsAdapter
    hotDealsAdapter.submitList(list ?: emptyList())
}

@BindingAdapter("indicatorRecyclerView", "data")
fun bindCircleIndicator(
    indicator: CircleIndicator3,
    rvFeatured: RecyclerView,
    featuredOffers: List<FeaturedOffer>?
) {
    val size = featuredOffers?.size ?: 0
    indicator.createIndicators(size, 0)

    rvFeatured.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
            val firstVisibleItemPosition = layoutManager?.findFirstVisibleItemPosition() ?: 0
            indicator.animatePageSelected(firstVisibleItemPosition)
        }
    })
}

@BindingAdapter("showShimmer")
fun showShimmer(shimmerRecyclerView: ShimmerRecyclerView, showShimmer: Boolean) {
    if (showShimmer)
        shimmerRecyclerView.showShimmer()
    else
        shimmerRecyclerView.hideShimmer()
}



