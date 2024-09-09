package com.easyEats.ezeats.ui.main.restaurant

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
            dismiss()
        }
        binding.tvShare.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "https://www.ezeats.com/restaurant/${viewModel.restaurantId}")
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

    }

    override fun listenToObservers() {
        viewModel.specialOffers.observe(viewLifecycleOwner) { resources ->
            when (resources) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    binding.restaurantInfo=resources.data?.result?.data
                    viewModel.setRestaurantData(
                        binding,
                        specialOfferAdapter,
                        resources.data?.result?.data?.offers?.size?:0,
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
}
