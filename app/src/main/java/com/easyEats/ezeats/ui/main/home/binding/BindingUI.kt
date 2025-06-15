package com.easyEats.ezeats.ui.main.home.binding

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.MotionEvent
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    private val fragment: HomeScreenFragment,
    private val lifecycleScope: LifecycleCoroutineScope,
) {
    fun invoke() {
        bindingData()
    }

    @SuppressLint("ClickableViewAccessibility")
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
        autoScrollRecyclerView(binding.rvOurPartners)
        autoScrollRecyclerView(binding.rvSpinWheelPartners)
//        binding.rvSpinWheelPartners.setOnTouchListener { _, _ -> true }


        // Set up click listeners for various UI elements
        binding.tvLocation.setOnClickListener {
            lifecycleScope.launch {
                binding.tvLocation.isClickable = false
                navController.navigate(R.id.location_bottom_sheet)
                delay(1000)
                binding.tvLocation.isClickable = true
            }

        }

        binding.tvViewAllTrackOrder.setOnClickListener {
            lifecycleScope.launch {
                binding.tvLocation.isClickable = false
                navController.navigate(R.id.goToActiveOrderDetails)
                delay(1000)
                binding.tvLocation.isClickable = true
            }
        }

        binding.etSearch.setOnClickListener {
            lifecycleScope.launch {
                binding.tvLocation.isClickable = false
                navController.navigate(R.id.goToSearchHistoryFragment)
                delay(1000)
                binding.tvLocation.isClickable = true
            }
            sendEvent("SignIn", "SignIn", "SignIn")  // Send event for sign-up action
        }

        binding.swipeToRefresh.setOnRefreshListener {

            homeViewModel.checkLocationPermission()  // Refresh the location permissions
        }

        binding.btnEnableAccess.setOnClickListener {
            openLocationSettings()  // Open location settings if not enabled
        }

        binding.tvViewAllFeatured.setOnClickListener {
            lifecycleScope.launch {
                binding.tvLocation.isClickable = false
                val bottomNavigationView = context.findViewById<BottomNavigationView>(R.id.nav_view)
                bottomNavigationView?.selectedItemId = R.id.percentScreenFragment
                navController.navigate(R.id.offers_fragment)
                delay(1000)
                binding.tvLocation.isClickable = true
            }

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

    private fun autoScrollRecyclerView(recyclerView: RecyclerView) {
        val handler = Handler(Looper.getMainLooper())

        val scrollRunnable = object : Runnable {
            override fun run() {
                if (!recyclerView.canScrollVertically(1) && !recyclerView.canScrollVertically(-1)) {
                    // Only auto-scroll horizontally when the user is not scrolling vertically
                    recyclerView.smoothScrollBy(12, 0) // Adjust speed here
                }

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

                // Reset to the beginning when reaching the end
                if (lastVisiblePosition == recyclerView.adapter?.itemCount?.minus(1)) {
                    recyclerView.postDelayed({
                        recyclerView.scrollToPosition(0)
                    }, 200)
                }

                handler.postDelayed(this, 50) // Adjust delay for smoothness
            }
        }

        handler.post(scrollRunnable)
    }


}
