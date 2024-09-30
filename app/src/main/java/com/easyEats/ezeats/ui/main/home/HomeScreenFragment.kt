package com.easyEats.ezeats.ui.main.home

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.os.Bundle
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
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
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

    // Register ActivityResultLauncher
    private lateinit var locationSettingsLauncher: ActivityResultLauncher<IntentSenderRequest>

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeScreenBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askUserToEnableLocation()


        locationSettingsLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) {
            homeViewModel.checkLocationPermission()
        }
    }

    override fun onResume() {
        super.onResume()
        observeViewModelData()
    }

    override fun setUpViews() {
        // Bind ViewModels and Adapters to the layout
        binding.mainViewModel = mainViewModel
        binding.homeViewModel = homeViewModel
        binding.featuredAdapter = featuredOfferAdapter
        binding.hotDealsAdapter = hotDealsOfferAdapter

        // Inject LocationUtil and set it in the ViewModel
        homeViewModel.setLocationUtil(LocationUtil(this))
//        homeViewModel.checkLocationPermission()

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
            // Handle action for Hot Deals View All
        }
    }

    private fun openLocationSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + context?.packageName)
        startActivity(intent)
        requireActivity().finish()
    }

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
                    // Show a loading indicator
                }

                is Resource.Error -> {
                    Toasty.error(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }

                is Resource.Success -> {
                    binding.swipeToRefresh.isRefreshing = false
                }

                is Resource.UnAuthenticated -> {
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
        // Handle click event for feature
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideMainLoading()
    }

    private fun askUserToEnableLocation() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 1000L
        ).build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(requireActivity())
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            homeViewModel.checkLocationPermission()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution).build()
                    locationSettingsLauncher.launch(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Toast.makeText(requireContext(), "Error: ${sendEx.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
