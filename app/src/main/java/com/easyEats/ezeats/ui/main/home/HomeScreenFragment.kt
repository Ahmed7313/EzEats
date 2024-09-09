package com.easyEats.ezeats.ui.main.home

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentHomeScreenBinding
import com.easyEats.ezeats.ui.main.home.adapter.featured.FeaturedOfferAdapter
import com.easyEats.ezeats.ui.main.home.adapter.hot_deals.HotDealsAdapter
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.utils.Resource
import com.easyEats.ezeats.utils.location.LocationUtil
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import android.provider.Settings
import android.util.Log
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.google.android.material.bottomnavigation.BottomNavigationView

@AndroidEntryPoint
class HomeScreenFragment : BaseFragment<FragmentHomeScreenBinding>(),
    HotDealsAdapter.OnHotDealClicked, FeaturedOfferAdapter.OnFeaturedClicked {

    // ViewModels for managing UI-related data across the activity and fragment
    val mainViewModel: MainViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    // Adapters for the RecyclerViews
    private val featuredOfferAdapter: FeaturedOfferAdapter by lazy { FeaturedOfferAdapter(this) }
    private val hotDealsOfferAdapter: HotDealsAdapter by lazy { HotDealsAdapter(this) }

    /**
     * Inflate the binding object for this fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container The parent view that the fragment's UI should be attached to
     * @return The inflated binding object for this fragment
     */
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeScreenBinding.inflate(inflater, container, false)

    /**
     * Called when the fragment is visible to the user and actively running.
     * This method sets up data observation and checks for location permissions.
     */
    override fun onResume() {
        super.onResume()
        observeViewModelData()
    }


    /**
     * Initializes views and sets up click listeners. This method is called
     * during the setup phase of the fragment lifecycle.
     */
    override fun setUpViews() {
        // Bind ViewModels and Adapters to the layout
        binding.mainViewModel = mainViewModel
        binding.homeViewModel = homeViewModel
        binding.featuredAdapter = featuredOfferAdapter
        binding.hotDealsAdapter = hotDealsOfferAdapter

        // Inject LocationUtil and set it in the ViewModel
        homeViewModel.setLocationUtil(LocationUtil(this))

        homeViewModel.checkLocationPermission()

        // Setup click listeners for location and search
        binding.tvLocation.setOnClickListener {
            findNavController().navigate(R.id.location_bottom_sheet)
        }

        binding.etSearch.setOnClickListener {
            findNavController().navigate(R.id.goToSearchHistoryFragment)
        }
        binding.swipeToRefresh.setOnRefreshListener {
            homeViewModel.checkLocationPermission()
        }
        binding.btnEnableAccess.setOnClickListener {
            openLocationSettings()
        }

        binding.tvViewAllFeatured.setOnClickListener {
            val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
            bottomNavigationView?.selectedItemId = R.id.percentScreenFragment
            findNavController().navigate(R.id.offers_fragment)
        }

        binding.tvViewAllHotDeals.setOnClickListener {
//            val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
//            bottomNavigationView?.selectedItemId = R.id.percentScreenFragment
//            findNavController().navigate(R.id.offers_fragment)
        }
    }

    private fun openLocationSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + context?.packageName)
        startActivity(intent)
        requireActivity().finish()
    }

    /**
     * Observes LiveData objects in the ViewModel and handles UI updates based on the data changes.
     * This method is called to observe data during the fragment's lifecycle.
     */
    private fun observeViewModelData() {

        mainViewModel.isOpenShareLink.observe(viewLifecycleOwner) { data ->
            if (data?.get("isOpenShareLink") == true) {
                Log.d(TAG, "observeViewModelData: ${data["restaurantId"].toString()}")
                findNavController().navigate(
                    HomeScreenFragmentDirections.goToRestaurantFragment(
                        data["restaurantId"].toString()
                    )
                )
                mainViewModel.isOpenShareLink.value = null
            }

        }

        homeViewModel.homeResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Handle loading state, e.g., show a progress   indicator
                }

                is Resource.Error -> {
                    // Show an error message using Toasty
                    Toasty.error(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }

                is Resource.Success -> {
                    // Handle success state, update UI with the fetched data
                    binding.swipeToRefresh.isRefreshing = false
                }

                is Resource.UnAuthenticated -> {
                    // Handle unauthenticated state, prompt user to log in
                    authenticateUser()
                }
            }
        }

        mainViewModel.userLocationAddressMutableLiveData.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvLocation.text =
                    ("You're at " + it.address) ?: getString(R.string.select_location)
                homeViewModel.checkLocationPermission()
            }
        }
    }

    override fun onHotDealClickListener(offer: HotDeal) {
        findNavController().navigate(HomeScreenFragmentDirections.goToRedeemScreen(offer.id.toString()))
    }

    override fun onFeatureClicked(offerId: String) {
//        findNavController().navigate(HomeScreenFragmentDirections.goToRedeemScreen(offerId))

    }
}
