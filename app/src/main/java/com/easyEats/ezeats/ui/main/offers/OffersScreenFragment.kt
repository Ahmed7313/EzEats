package com.easyEats.ezeats.ui.main.offers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentPercentScreenBinding
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.new_restaurants_reponse.RestaurantsResponseItem
import com.easyEats.ezeats.domain.main.model.response.search_response.Category
import com.easyEats.ezeats.ui.compose.offers.OffersList
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.ui.main.offers.adapter.OffersCategoryAdapter
import com.easyEats.ezeats.utils.Constants
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OffersScreenFragment : BaseFragment<FragmentPercentScreenBinding>(),
    OffersCategoryAdapter.OnCategoryClickListener {

    override val shouldRetainView: Boolean = true

    private val mainViewModel: MainViewModel by activityViewModels()
    private val offersViewModel: OffersViewModel by viewModels()
    private lateinit var cateAdapter: OffersCategoryAdapter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPercentScreenBinding.inflate(inflater, container, false)

    override fun setUpViews() {
        binding.tvLocation.setOnClickListener {
            findNavController().navigate(R.id.location_bottom_sheet)
        }
        binding.ivSearch.setOnClickListener {
            findNavController().navigate(R.id.goToSearchHistoryFragment)
        }
        getCategoriesAndRestaurants()
        handleBackDirection()
    }

    private fun getCategoriesAndRestaurants() {
        offersViewModel.getCategoriesRestaurantResult()
        offersViewModel.getRestaurantsResult(
            "",
            mainViewModel.userLocationAddressMutableLiveData.value?.lat,
            mainViewModel.userLocationAddressMutableLiveData.value?.lng
        )
    }

    private fun handleBackDirection() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.homeScreenFragment)
                }
            }
        )
    }

    override fun listenToObservers() {
        listenToCurrentLocation()
        listenToCategories()
        listenToRestaurants()
    }

    private fun listenToCategories() {
        offersViewModel.categoryResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    cateAdapter = OffersCategoryAdapter(this)
                    offersViewModel.setDataOfCategories(binding, cateAdapter, resource)
                }
                is Resource.Error -> {
                    hideMainLoading()
                    Toasty.error(requireContext(), resource.message ?: "", Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> showMainLoading()
                is Resource.UnAuthenticated -> hideMainLoading()
            }
        }
    }

    private fun listenToRestaurants() {
        lifecycleScope.launch {
            offersViewModel.restaurantResultStateFlow.collect { resource ->
                when (resource) {
                    is Resource.Loading -> showMainLoading()

                    is Resource.Error -> {
                        hideMainLoading()
                        Toasty.error(requireContext(), resource.message ?: "", Toast.LENGTH_LONG).show()
                    }

                    is Resource.Success -> {
                        hideMainLoading()
                        val list = resource.data?.result?.data ?: emptyList<RestaurantsResponseItem>()
                        binding.composeRestaurantList.setContent {
                            OffersList(
                                restaurants = list,
                                onRestaurantClick = { restaurant ->
                                    findNavController().navigate(
                                        OffersScreenFragmentDirections
                                            .goToRestaurantFragment(restaurant.id!!)
                                    )
                                }
                            )
                        }
                    }

                    is Resource.UnAuthenticated -> hideMainLoading()
                }
            }
        }
    }

    private fun listenToCurrentLocation() {
        lifecycleScope.launch {
            mainViewModel.userLocationAddressMutableLiveData.collect { loc ->
                binding.tvLocation.text = loc?.address?.let { "You're at $it" }
                    ?: getString(R.string.select_location)
                offersViewModel.getRestaurantsResult(
                    "",
                    loc?.lat,
                    loc?.lng
                )
            }
        }
    }

    override fun onCategoryClicked(category: Category) {
        val id = category.categoryId ?: Constants.CATEGORY_ID_ALL
        offersViewModel.getRestaurantsResult(
            id,
            mainViewModel.userLocationAddressMutableLiveData.value?.lat,
            mainViewModel.userLocationAddressMutableLiveData.value?.lng
        )
    }

    override fun onPause() {
        super.onPause()
        hideMainLoading()
    }
}
