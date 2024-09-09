package com.easyEats.ezeats.ui.main.search_result

import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseBottomSheetFragment
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.databinding.FragmentSearchResultBinding
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.Restaurant
import com.easyEats.ezeats.ui.main.home.HomeScreenFragmentDirections
import com.easyEats.ezeats.ui.main.home.adapter.hot_deals.HotDealsAdapter
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.ui.main.search_result.adapter.OfferAdapter
import com.easyEats.ezeats.ui.main.search_result.adapter.RestaurantAdapter
import com.easyEats.ezeats.ui.main.search_suggestion.SearchFragmentDirections
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty


/**
 * A Fragment that displays search results, including Restaurants and Hot Deals.
 * This fragment uses data binding and observes LiveData from a ViewModel to update the UI
 * based on the user's location and search results.
 *
 * This fragment extends `BaseBottomSheetFragment`, leveraging its capabilities to provide
 * a bottom sheet UI component.
 */
@AndroidEntryPoint
class SearchResultFragment : BaseBottomSheetFragment<FragmentSearchResultBinding>(),
    RestaurantAdapter.OnRestaurantClicked, HotDealsAdapter.OnHotDealClicked,
    OfferAdapter.OnOfferClicked {

    // ViewModel shared with other fragments/activities to observe the user's location
    private val mainViewModel: MainViewModel by activityViewModels()

    // ViewModel specific to this fragment for managing search results and UI state
    private val searchResultViewModel: SearchResultViewModel by viewModels()

    // Adapter for displaying a list of restaurants in the search results
    private lateinit var restaurantAdapter: RestaurantAdapter

    // Adapter for displaying a list of hot deals in the search results
    private lateinit var hotDealsAdapter: HotDealsAdapter

    /**
     * Inflates the fragment's view binding.
     *
     * @param inflater The LayoutInflater used to inflate the binding.
     * @param container The parent ViewGroup into which the fragment's view will be attached.
     * @return The inflated binding for this fragment.
     */
    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentSearchResultBinding.inflate(layoutInflater)

    /**
     * Sets up the views and their initial states, including setting up adapters and click listeners.
     * This method is called after the fragment's view is created.
     */
    override fun setUpViews() {
        // Initialize the adapters for restaurant and hot deals RecyclerViews
        restaurantAdapter = RestaurantAdapter(this, this)
        hotDealsAdapter = HotDealsAdapter(this)

        // Bind the restaurant adapter to the RecyclerView in the layout
        binding.rvRestaurantSearch.adapter = restaurantAdapter

        // Bind the ViewModel to the layout
        binding.searchResultViewModel = searchResultViewModel

        // Set up click listener for the search button to dismiss the bottom sheet
        binding.mcvSearch.setOnClickListener { dismiss() }

        // Set up click listener for the location text to navigate to the location selection bottom sheet
        binding.tvLocation.setOnClickListener { findNavController().navigate(R.id.location_bottom_sheet) }

        // Set up click listener for the explore button to change the view type (toggle between Restaurants and Offers)
        binding.btnExplore.setOnClickListener { searchResultViewModel.changeViewType() }
    }

    /**
     * Observes LiveData from the ViewModels and updates the UI accordingly.
     * This method is called after the fragment's view is created and the ViewModel is set up.
     */
    override fun listenToObservers() {
        // Observe changes in the user's location and update the location text view
        mainViewModel.userLocationAddressMutableLiveData.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvLocation.text =
                    ("You're at " + it.address) ?: getString(R.string.select_location)

            }
        }

        // Observe changes in the view type (Restaurant/Offer) and update the UI accordingly
        searchResultViewModel.viewType.observe(viewLifecycleOwner) { viewType ->
            searchResultViewModel.listenToViewType(binding, viewType)
        }

        // Observe the restaurant search results and update the UI accordingly
        searchResultViewModel.searchResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    // Show an error message if there is an error
                    Toasty.error(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }

                is Resource.Loading -> {
                    // Handle loading state if needed
                }

                is Resource.Success -> {
                    // Update the UI with the list of restaurants
                    searchResultViewModel.setRestaurantsData(
                        requireContext(),
                        binding,
                        restaurantAdapter,
                        resource
                    )
                }

                is Resource.UnAuthenticated -> {
                    // Handle unauthenticated state if needed
                }
            }
        }

        // Observe the hot deals search results and update the UI accordingly
        searchResultViewModel.searchOfferResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    // Show an error message if there is an error
                    Toasty.error(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }

                is Resource.Loading -> {
                    // Handle loading state if needed
                }

                is Resource.Success -> {
                    // Update the UI with the list of hot deals
                    searchResultViewModel.setOffersData(
                        requireContext(),
                        binding,
                        hotDealsAdapter,
                        resource
                    )
                }

                is Resource.UnAuthenticated -> {
                }
            }
        }
    }

    override fun onRestaurantClickListener(restaurant: Restaurant) {
        findNavController().navigate(
            SearchResultFragmentDirections.goToRestaurantFragment(restaurant.id!!)
        )
    }

    override fun onHotDealClickListener(offer: HotDeal) {
        findNavController().navigate(SearchResultFragmentDirections.goToRedeemScreen(offer.id.toString()))
    }

    override fun onOfferClicked(offerId: String) {
        findNavController().navigate(SearchResultFragmentDirections.goToRedeemScreen(offerId))
    }
}
