package com.easyEats.ezeats.ui.main.restaurant

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentRestaurantBinding
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.RestaurantInfo
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.new_restaurants_reponse.RestaurantsResponseItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantFragment : BaseFragment<FragmentRestaurantBinding>() {

    override val shouldRetainView: Boolean = true

    private val args by navArgs<RestaurantFragmentArgs>()
    private lateinit var restaurant: RestaurantsResponseItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restaurant = args.restaurant
    }

    override fun setUpViews() {
        binding.btnDismiss.setOnClickListener { openBrowser() }
        binding.tvShare.setOnClickListener { shareRestaurantLink() }
        binding.ivBack.setOnClickListener { findNavController().navigateUp() }
        binding.restaurantInfo = restaurant.toRestaurantInfo()
    }

    override fun listenToObservers() { /* No observers */ }

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRestaurantBinding.inflate(inflater, container, false)

    private fun RestaurantsResponseItem.toRestaurantInfo() = RestaurantInfo(
        restaurantName = this.name,
        restaurantLogo = this.logo,
        restaurantCover = this.logo,
        distance = this.distance,
        reviewsCount = this.reviewsCount,
        rating = this.rating,
        offers = emptyList()
    )

    private fun shareRestaurantLink() {
        val restaurantId = restaurant.id
        val shareText =
            "Check out this restaurant on Ezeats: https://www.ezeats.app/restaurant/$restaurantId"
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val chooser = Intent.createChooser(shareIntent, "Share via")
        if (shareIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(chooser)
        } else {
            Toast.makeText(requireContext(), "No apps available to share", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun openBrowser() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://ezeats.app/${restaurant.id}-1")
        requireActivity().startActivity(intent)
    }
}
