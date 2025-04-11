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
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentPercentScreenBinding
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.new_restaurants_reponse.RestaurantsResponseItem
import com.easyEats.ezeats.domain.main.model.response.search_response.Category
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.ui.main.offers.adapter.OffersCategoryAdapter
import com.easyEats.ezeats.ui.main.search_result.adapter.OfferAdapter
import com.easyEats.ezeats.ui.main.search_result.adapter.RestaurantAdapter
import com.easyEats.ezeats.utils.Constants
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * OffersScreenFragment is responsible for displaying offers, categories, and restaurants.
 * It uses ViewModels, Adapters, and user interactions to manage the UI and data flow.
 */
@AndroidEntryPoint
class OffersScreenFragment : BaseFragment<FragmentPercentScreenBinding>(),
    OffersCategoryAdapter.OnCategoryClickListener, RestaurantAdapter.OnRestaurantClicked,
    OfferAdapter.OnOfferClicked {
    override val shouldRetainView: Boolean = true

    // ViewModels for managing data
    private val mainViewModel: MainViewModel by activityViewModels()
    private val offersViewModel: OffersViewModel by viewModels()

    // Adapters for RecyclerViews
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var cateAdapter: OffersCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getCategoriesAndRestaurants()
    }

    /**
     * Inflates the binding for the fragment.
     */
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPercentScreenBinding.inflate(layoutInflater)

    /**
     * Sets up views and initializes UI components.
     */
    override fun setUpViews() {
        with(binding) {
            // Navigate to location selection
            tvLocation.setOnClickListener {
                findNavController().navigate(R.id.location_bottom_sheet)
            }

            // Navigate to search history
            ivSearch.setOnClickListener {
                findNavController().navigate(R.id.goToSearchHistoryFragment)
            }
        }
        getCategoriesAndRestaurants()
        handleBackDirection()
    }

    /**
     * Fetches categories and restaurant data from the ViewModel.
     */
    private fun getCategoriesAndRestaurants() {
            offersViewModel.getCategoriesRestaurantResult()
            offersViewModel.getRestaurantsResult(
                "",
                mainViewModel.userLocationAddressMutableLiveData.value?.lat,
                mainViewModel.userLocationAddressMutableLiveData.value?.lng
            )
    }

    /**
     * Handles the back button press to navigate to the home screen.
     * This is a workaround for devices where the back button might not function correctly.
     */
    private fun handleBackDirection() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.homeScreenFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    /**
     * Observes LiveData and StateFlow objects for UI updates.
     */
    override fun listenToObservers() {
        listenToCurrentLocation()
        listenToRestaurants()
        listenToCategories()
    }

    /**
     * Observes LiveData and StateFlow objects for UI updates
     */
    private fun listenToCategories() {
        offersViewModel.categoryResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    Toasty.error(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                    hideMainLoading()
                }

                is Resource.Loading -> {
                    showMainLoading()
                }

                is Resource.Success -> {
                    cateAdapter = OffersCategoryAdapter(this)
                    offersViewModel.setDataOfCategories(binding, cateAdapter, resource)
                }

                is Resource.UnAuthenticated -> {
                    hideMainLoading()
                }
            }
        }
    }

    /**
     * Observes LiveData and StateFlow objects for UI updates
     */
    private fun listenToRestaurants() {
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
    }

    private fun listenToCurrentLocation() {
        lifecycleScope.launch {
            mainViewModel.userLocationAddressMutableLiveData.collect {
                binding.tvLocation.text = if (it?.address != null) {
                    "You're at ${it.address}"
                } else {
                    getString(R.string.select_location)
                }
                offersViewModel.getRestaurantsResult("", it?.lat, it?.lng)
            }
        }
    }

    /**
     * Handles category click events.
     */
    override fun onCategoryClicked(category: Category) {
        // If the clicked category is "All", get all restaurants
        if (category.categoryId == Constants.CATEGORY_ID_ALL)
            offersViewModel.getRestaurantsResult(
                "", mainViewModel.userLocationAddressMutableLiveData.value?.lat,
                mainViewModel.userLocationAddressMutableLiveData.value?.lng
            )
        // Else get restaurants for the clicked category
        else
            offersViewModel.getRestaurantsResult(
                category.categoryId!!,
                mainViewModel.userLocationAddressMutableLiveData.value?.lat,
                mainViewModel.userLocationAddressMutableLiveData.value?.lng
            )
    }

    /**
     * Handles restaurant item click events.
     */
    override fun onRestaurantClickListener(
        restaurant: RestaurantsResponseItem,
        cl: ConstraintLayout
    ) {
        lifecycleScope.launch {
            cl.isClickable = false
            delay(1000)
            Log.d("TAG", "onRestaurantClickListener: ${restaurant.id}")
            findNavController().navigate(
                OffersScreenFragmentDirections.goToRestaurantFragment(
                    restaurant.id!!
                )
            )
            delay(500)
            cl.isClickable = true
        }
    }

    /**
     * Handles offer click events.
     */
    override fun onOfferClicked(offerId: String) {
//        findNavController().navigate(OffersScreenFragmentDirections.goToRedeemScreen(offerId))
    }

    /**
     * Hides the main loading indicator when the fragment is paused.
     */
    override fun onPause() {
        super.onPause()
        hideMainLoading()
    }
}
