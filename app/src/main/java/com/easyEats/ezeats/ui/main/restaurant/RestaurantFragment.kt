package com.easyEats.ezeats.ui.main.restaurant

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseBottomSheetFragment
import com.easyEats.ezeats.databinding.FragmentRestaurantBinding
import com.easyEats.ezeats.ui.main.restaurant.adapter.SpecialOfferAdapter
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantFragment : BaseBottomSheetFragment<FragmentRestaurantBinding>(),
    SpecialOfferAdapter.OnSpecialOfferClicked {

    private val viewModel: RestaurantViewModel by viewModels()
    private lateinit var specialOfferAdapter: SpecialOfferAdapter


    override fun setUpViews() {
        specialOfferAdapter = SpecialOfferAdapter(this)
        binding.btnDismiss.setOnClickListener {
            openBrowser()
        }
        binding.tvShare.setOnClickListener {
            val restaurantId = viewModel.restaurantId
            val shareText =
                "Check out this restaurant on Ezeats: https://www.ezeats.app/restaurant/$restaurantId"

            // Create the share intent
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }

            // Start the activity with a chooser
            val chooser = Intent.createChooser(shareIntent, "Share via")
            if (shareIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(chooser)
            } else {
                // Handle the case where no apps can handle the intent
                Toast.makeText(requireContext(), "No apps available to share", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    override fun listenToObservers() {
        viewModel.specialOffers.observe(viewLifecycleOwner) { resources ->
            when (resources) {
                is Resource.Error -> {}
                is Resource.Loading -> {
                    binding.loader.visibility = VISIBLE
                }

                is Resource.Success -> {
                    binding.loader.visibility = GONE
                    binding.restaurantInfo = resources.data?.result?.data
                    viewModel.setRestaurantData(
                        binding,
                        specialOfferAdapter,
                        resources.data?.result?.data?.offers?.size ?: 0,
                        resources.data?.result?.data?.offers
                    )

                }

                is Resource.UnAuthenticated -> {}
            }
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRestaurantBinding.inflate(layoutInflater)

    override fun onSpecialOfferClickListener(offerId: String) {
        findNavController().navigate(RestaurantFragmentDirections.goToRedeemScreen(offerId))
    }

    private fun openBrowser() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://ezeats.app/${viewModel.restaurantId}-1")
        requireActivity().startActivity(intent)
    }
}
