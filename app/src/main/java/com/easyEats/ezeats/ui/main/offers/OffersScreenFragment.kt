package com.easyEats.ezeats.ui.main.offers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.MyApplication
import com.easyEats.ezeats.MyApplication.Companion
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.databinding.FragmentPercentScreenBinding
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.Restaurant
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.new_restaurants_reponse.RestaurantsResponseItem
import com.easyEats.ezeats.domain.main.model.response.search_response.Category
import com.easyEats.ezeats.ui.main.home.HomeViewModel
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.ui.main.offers.adapter.OffersCategoryAdapter
import com.easyEats.ezeats.ui.main.search_result.adapter.OfferAdapter
import com.easyEats.ezeats.ui.main.search_result.adapter.RestaurantAdapter
import com.easyEats.ezeats.utils.Resource
import com.makeramen.roundedimageview.RoundedImageView.TAG
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.log


@AndroidEntryPoint
class OffersScreenFragment : BaseFragment<FragmentPercentScreenBinding>(),
    OffersCategoryAdapter.OnCategoryClickListener, RestaurantAdapter.OnRestaurantClicked,
    OfferAdapter.OnOfferClicked {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val offersViewModel: OffersViewModel by viewModels()
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var cateAdapter: OffersCategoryAdapter


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPercentScreenBinding.inflate(layoutInflater)

    override fun setUpViews() {
        with(binding) {
            tvLocation.setOnClickListener {
                android.util.Log.d(TAG, "setUpViews: test")
                findNavController().navigate(R.id.location_bottom_sheet) }
            ivSearch.setOnClickListener { findNavController().navigate(R.id.goToSearchHistoryFragment) }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back press event
                Log.d(TAG, "handleOnBackPressed: ")
                findNavController().navigate(R.id.homeScreenFragment)
            }
        }

        // Add the callback to the OnBackPressedDispatcher
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


    override fun listenToObservers() {
        lifecycleScope.launch {
            mainViewModel.fetchCurrentAddress().collect {
                binding.tvLocation.text = if (it?.address !=null) {
                    "You're at ${it?.address}"
                } else {
                    getString(R.string.select_location)
                }
                offersViewModel.getRestaurantsResult("")
                offersViewModel.getCategoriesRestaurantResult()
            }

        }


        lifecycleScope.launch {
            offersViewModel.restaurantResultStateFlow.collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        hideMainLoading()
                        Toasty.error(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                    }

                    is Resource.Loading -> {
                        showMainLoading()
                    }

                    is Resource.Success -> {
                      restaurantAdapter =
                          RestaurantAdapter(this@OffersScreenFragment, this@OffersScreenFragment)
                        resource.data?.result?.data?.let {
                            offersViewModel.setRestaurantsData(
                                requireContext(),
                                binding,
                                restaurantAdapter,
                                it
                            )
                        }
                        hideMainLoading()
                    }

                    is Resource.UnAuthenticated -> {
                        hideMainLoading()
                    }
                }
            }
        }


        offersViewModel.categoryResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    Toasty.error(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                    hideMainLoading()
                }

                is Resource.Loading -> {
//                    binding.srGetOffers.isRefreshing = false
                    showMainLoading()
                }

                is Resource.Success -> {
                    cateAdapter = OffersCategoryAdapter(this)
                    offersViewModel.setDataOfCategories(binding, cateAdapter, resource)
                    Log.d(TAG, "listenToObservers: setDataOfCategories")
                }

                is Resource.UnAuthenticated -> {
                    hideMainLoading()
                }
            }
        }
    }


    override fun onCategoryClicked(category: Category) {
        Log.d(TAG, "onCategoryClicked: ${category.categoryId}")
        offersViewModel.isRequestFired=false
        if (category.categoryId == "eqvWcHcHaD")
            offersViewModel.getRestaurantsResult("")
        else
            offersViewModel.getRestaurantsResult(category.categoryId!!)
    }

    override fun onRestaurantClickListener(restaurant: RestaurantsResponseItem,cl:ConstraintLayout) {
        lifecycleScope.launch {
            cl.isClickable = false
            delay(1000)
            findNavController().navigate(
                OffersScreenFragmentDirections.goToRestaurantFragment(
                    restaurant.id!!
                )
            )
            delay(500)
            cl.isClickable = true
        }

    }

    override fun onOfferClicked(offerId: String) {
        findNavController().navigate(OffersScreenFragmentDirections.goToRedeemScreen(offerId))
    }

    override fun onPause() {
        super.onPause()
        hideMainLoading()
    }

}