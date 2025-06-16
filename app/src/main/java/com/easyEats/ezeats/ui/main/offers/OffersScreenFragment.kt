package com.easyEats.ezeats.ui.main.offers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.daman.edman.screens.components.AppSpacer
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentPercentScreenBinding
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.new_restaurants_reponse.RestaurantsResponseItem
import com.easyEats.ezeats.domain.main.model.response.search_response.Category
import com.easyEats.ezeats.ui.compose.offers.OffersList
import com.easyEats.ezeats.ui.compose.offers.SelectionButton
import com.easyEats.ezeats.ui.large
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.ui.main.offers.adapter.OffersCategoryAdapter
import com.easyEats.ezeats.ui.spacing
import com.easyEats.ezeats.utils.Constants
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch
import kotlin.collections.buildList

@AndroidEntryPoint
class OffersScreenFragment : BaseFragment<FragmentPercentScreenBinding>(),
    OffersCategoryAdapter.OnCategoryClickListener {

    //override val shouldRetainView: Boolean = true

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
        // This is the correct and safe way to collect a StateFlow from a Fragment's UI
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // This will now automatically re-collect when you navigate back to the screen
                offersViewModel.restaurantResultStateFlow.collect { resource ->
                    Log.v(
                        "Fragment_Debug",
                        "Collector received a new resource: ${resource::class.java.simpleName}"
                    )

                    when (resource) {
                        is Resource.Loading -> showMainLoading()

                        is Resource.Error -> {
                            hideMainLoading()
                            Toasty.error(
                                requireContext(),
                                resource.message ?: "",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        is Resource.Success -> {
                            hideMainLoading()
                            val list = buildList {
                                addAll(
                                    resource.data?.result?.data?.restaurants?.pickupRestaurants
                                        ?: emptyList()
                                )
                                addAll(
                                    resource.data?.result?.data?.restaurants?.deliveryRestaurants
                                        ?: emptyList()
                                )
                                addAll(
                                    resource.data?.result?.data?.restaurants?.dineinRestaurants
                                        ?: emptyList()
                                )
                            }.distinctBy { it.id }
                            Log.v(
                                "Fragment_Debug",
                                "SUCCESS: Received a list of size: ${list.size}. Calling setContent."
                            )

                            // Check if the fragment's view is still active
                            if (view != null) {
                                binding.composeRestaurantList.setContent {

                                    var selectedTab by remember { mutableStateOf(0) }


                                    Column(modifier = Modifier) {

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            SelectionButton(
                                                modifier = Modifier.weight(1f),
                                                text = getString(R.string.pickup),
                                                icon = R.drawable.ic_dish,
                                                isSelected = selectedTab == 0,
                                                onSelect = { selectedTab = 0 }
                                            )

                                            Spacer(modifier = Modifier.width(8.dp))

                                            SelectionButton(
                                                modifier = Modifier.weight(1f),
                                                text = getString(R.string.delivery),
                                                isSelected = selectedTab == 1,
                                                onSelect = { selectedTab = 1 },
                                                icon = R.drawable.ic_delivery,

                                                )
                                        }

                                        AppSpacer(height = large   )

                                        val filteredList = when (selectedTab) {
                                            0 -> list.filter { it.isPickUp == true }
                                            1 -> list.filter { it.isDelivery == true }
                                            else -> list
                                        }

                                        OffersList(
                                            restaurants = filteredList,
                                            onRestaurantClick = { restaurant ->
                                                findNavController().navigate(
                                                    OffersScreenFragmentDirections
                                                        .goToRestaurantFragment(restaurant.id!!)
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        is Resource.UnAuthenticated -> hideMainLoading()
                    }
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
                    //loc?.lat,
                    //loc?.lng
                    30.0145125310423.toString(),
                    30.975965400415998.toString()
                )
            }
        }
    }

    override fun onCategoryClicked(category: Category) {
        val id = category.categoryId ?: Constants.CATEGORY_ID_ALL
        offersViewModel.getRestaurantsResult(
            mainViewModel.userLocationAddressMutableLiveData.value?.lat,
            mainViewModel.userLocationAddressMutableLiveData.value?.lng
        )
    }

    override fun onPause() {
        super.onPause()
        hideMainLoading()
    }
}
