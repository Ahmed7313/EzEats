package com.easyEats.ezeats.ui.main.home.binding

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearSnapHelper
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.FragmentHomeScreenBinding
import com.easyEats.ezeats.ui.main.home.HomeScreenFragment
import com.easyEats.ezeats.ui.main.home.HomeViewModel
import com.easyEats.ezeats.ui.main.home.adapter.featured.FeaturedOfferAdapter
import com.easyEats.ezeats.ui.main.home.adapter.hot_deals.HotDealsAdapter
import com.easyEats.ezeats.ui.main.home.adapter.partners.PartnersAdapter
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.utils.location.LocationUtil
import com.easyEats.ezeats.utils.tracking.Event.sendEvent
import com.google.android.material.bottomnavigation.BottomNavigationView

data class BindingUI(
    private val binding: FragmentHomeScreenBinding,
    private val mainViewModel: MainViewModel,
    private val homeViewModel: HomeViewModel,
    private val featuredOfferAdapter: FeaturedOfferAdapter,
    private val ourPartnersAdapter: PartnersAdapter,
    private val spinWheelPartnersAdapter: PartnersAdapter,
    private val snapHelper: LinearSnapHelper,
    private val hotDealsOfferAdapter: HotDealsAdapter,
    private val navController: NavController,
    private val context: Activity,
    private val fragment: HomeScreenFragment

) {
    fun invoke(){
        bindingData()
    }

    private fun bindingData() {
        binding.mainViewModel = mainViewModel
        binding.homeViewModel = homeViewModel
        binding.featuredAdapter = featuredOfferAdapter
        binding.partnersAdapter = ourPartnersAdapter
        binding.spinWheelpartnersAdapter = spinWheelPartnersAdapter
        snapHelper.attachToRecyclerView(binding.rvFeatured)
        binding.hotDealsAdapter = hotDealsOfferAdapter

        // Inject LocationUtil and set it in the ViewModel
        homeViewModel.setLocationUtil(LocationUtil(fragment))

        // Set up click listeners for various UI elements
        binding.tvLocation.setOnClickListener {
            navController.navigate(R.id.location_bottom_sheet)
        }

        binding.tvViewAllTrackOrder.setOnClickListener {
            navController.navigate(R.id.goToActiveOrderDetails)
        }

        binding.etSearch.setOnClickListener {
            navController.navigate(R.id.goToSearchHistoryFragment)
            sendEvent("SignIn", "SignIn", "SignIn")  // Send event for sign-up action
        }

        binding.swipeToRefresh.setOnRefreshListener {
            homeViewModel.checkLocationPermission()  // Refresh the location permissions
        }

        binding.btnEnableAccess.setOnClickListener {
            openLocationSettings()  // Open location settings if not enabled
        }

        binding.tvViewAllFeatured.setOnClickListener {
            val bottomNavigationView = context.findViewById<BottomNavigationView>(R.id.nav_view)
            bottomNavigationView?.selectedItemId = R.id.percentScreenFragment
            navController.navigate(R.id.offers_fragment)
        }

        binding.tvViewAllHotDeals.setOnClickListener {
            // Handle action for Hot Deals View All
        }
    }

    // Function to open the app's location settings
    private fun openLocationSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + context.packageName)
        context.startActivity(intent)
        context.finish()
    }
}
