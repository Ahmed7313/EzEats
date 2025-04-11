package com.easyEats.ezeats.ui.main.home

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import android.view.LayoutInflater
import android.view.ViewGroup
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentHomeScreenBinding
import com.easyEats.ezeats.ui.main.home.adapter.featured.FeaturedOfferAdapter
import com.easyEats.ezeats.ui.main.home.adapter.hot_deals.HotDealsAdapter
import com.easyEats.ezeats.ui.main.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearSnapHelper
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.easyEats.ezeats.ui.main.complaint.ComplaintFragment
import com.easyEats.ezeats.ui.main.home.adapter.partners.PartnersAdapter
import com.easyEats.ezeats.ui.main.home.binding.BindingUI
import com.easyEats.ezeats.ui.main.home.listeners.HomeListeners
import com.easyEats.ezeats.ui.main.home.location.Location
import com.easyEats.ezeats.ui.main.home.notifications.Notification
import com.easyEats.ezeats.ui.main.rating.RateUsFragment
import com.easyEats.ezeats.utils.Resource
import kotlinx.coroutines.launch
@AndroidEntryPoint
class HomeScreenFragment : BaseFragment<FragmentHomeScreenBinding>(),
    HotDealsAdapter.OnHotDealClicked, FeaturedOfferAdapter.OnFeaturedClicked,
    RateUsFragment.DialogCallback {

    // ViewModels are injected using Hilt
    val mainViewModel: MainViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    // Adapters for different categories
    private val featuredOfferAdapter: FeaturedOfferAdapter by lazy { FeaturedOfferAdapter(this) }
    private val hotDealsOfferAdapter: HotDealsAdapter by lazy { HotDealsAdapter(this) }
    private val snapHelper = LinearSnapHelper()

    // Adapters for Partners section
    private val ourPartnersAdapter by lazy { PartnersAdapter() }
    private val spinWheelPartnersAdapter by lazy { PartnersAdapter() }

    // Location and notification setup
    private lateinit var locationSettingsLauncher: ActivityResultLauncher<IntentSenderRequest>
    private val notification by lazy {
        Notification(
            homeViewModel,
            requireActivity(),
            findNavController()
        )
    }

    private val location by lazy {
        Location(
            requireActivity(),
            homeViewModel,
            locationSettingsLauncher
        )
    }

    // BroadcastReceiver for handling notifications
    private val notificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateUIWithNotification()
        }
    }

    // Update UI after receiving notification
    private fun updateUIWithNotification() {
        homeViewModel.checkLocationPermission()
    }

    // Inflate the binding for the fragment
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeScreenBinding.inflate(inflater, container, false)

    // On fragment creation, handle notifications, location, and permissions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Fetch saved address for the user
        fetchSavedAddress()

        // Refresh Firebase token for notifications
        notification.refreshFirebaseToken()

        // Register launcher for location settings
        locationSettingsLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                homeViewModel.checkLocationPermission()
            }

        // Prompt user to enable location services
        location.askUserToEnableLocation()

        // Request notification permission on Android 13 and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                101
            )
            notification.pushNotification() {
                homeViewModel.checkLocationPermission()
            }
        }

        // Handle the type of notification to navigate to a specific screen
        val notificationType = requireActivity().intent.getStringExtra("type")
        notificationType?.let {
            notification.navigateToScreen(it)
        }
    }

    // Observe saved address
    private fun fetchSavedAddress() {
        lifecycleScope.launch {
            mainViewModel.fetchCurrentAddress().collect {
                Log.d(TAG, "onCreate: ${it?.lat}")
                Log.d(TAG, "onCreate: ${it?.lat}")
            }
        }
    }

    // Handle fragment resume state, register broadcast receiver and observe data
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onResume() {
        super.onResume()
        observeViewModelData()

        // Handle back press to finish the activity
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })

        // Register broadcast receiver to handle notification updates
        requireContext().registerReceiver(
            notificationReceiver,
            IntentFilter("com.example.UPDATE_UI"),
            Context.RECEIVER_NOT_EXPORTED
        )
    }

    // Set up views and bind UI components
    override fun setUpViews() {
        homeViewModel.getPartners()
        val bindingUI = BindingUI(
            binding,
            mainViewModel,
            homeViewModel,
            featuredOfferAdapter,
            ourPartnersAdapter,
            spinWheelPartnersAdapter,
            snapHelper,
            hotDealsOfferAdapter,
            findNavController(),
            requireActivity(),
            this,
            lifecycleScope
        )
        bindingUI.invoke()
    }

    // Observes changes in ViewModel data and updates the UI accordingly
    private fun observeViewModelData() {
        val listeners = HomeListeners(
            homeViewModel,
            mainViewModel,
            binding,
            requireActivity(),
            lifecycleScope,
            viewLifecycleOwner,
            findNavController(),
            this,
            ourPartnersAdapter,
            spinWheelPartnersAdapter
        )
        listeners.invoke()
    }

    // Handle click event for Hot Deals
    override fun onHotDealClickListener(offer: HotDeal) {
    }

    // Handle click event for Featured Offers
    override fun onFeatureClicked(offerId: String) {
        // Handle click event for feature
    }

    // Unregister the receiver when fragment is paused
    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(notificationReceiver)
    }

    // Show the "Rate Us" dialog
    fun displayRatingDialog() {
        val rateUsFragment = RateUsFragment()
        rateUsFragment.setDialogCallback(this)
        rateUsFragment.show(parentFragmentManager, "CustomDialog")
    }

    // Handle the "Rate Us" click event to open the Play Store
    override fun onRateUsClicked() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://play.google.com/store/apps/details?id=com.easyEats.ezeats")
        requireActivity().startActivity(intent)
    }

    // Handle the "Rate Later" click event (empty implementation for now)
    override fun onRateLaterClicked() {}
}


