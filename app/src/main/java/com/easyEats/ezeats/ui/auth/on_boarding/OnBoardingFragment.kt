package com.easyEats.ezeats.ui.auth.on_boarding

import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.MyApplication.Companion.baseApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentOnBoardingBinding
import com.easyEats.ezeats.domain.main.model.UserLocationAddress
import com.easyEats.ezeats.ui.auth.on_boarding.pager.IntroSlide
import com.easyEats.ezeats.ui.auth.on_boarding.pager.IntroSliderAdapter
import com.easyEats.ezeats.utils.Constants.LOCATION_PERMISSION_REQUEST_CODE
import com.easyEats.ezeats.utils.location.LocationUtil
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

/**
 * Fragment that handles the onboarding process of the application.
 * Displays introductory slides and requests location permission from the user.
 */
@AndroidEntryPoint
class OnBoardingFragment : BaseFragment<FragmentOnBoardingBinding>() {

    // ViewModel for managing onboarding data and logic
    private val viewModel: OnBoardingViewModel by viewModels()

    // Adapter for displaying introductory slides
    private lateinit var introSliderAdapter: IntroSliderAdapter

    // Utility class for handling location permission requests
    private var locationUtil: LocationUtil?=null

    /**
     * Inflates the layout for this fragment.
     */
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOnBoardingBinding.inflate(layoutInflater, container, false)

    /**
     * Sets up views and initializes necessary components.
     */
    override fun setUpViews() {
        changeStatusBackground()
        setupIntroSlider()
        setupLocationUtil()
        setupObservers()
        setupSkipButton()
        setupPageChangeCallback()
        viewModel.setIntroSlides(listOf(
            IntroSlide(
                getString(R.string.welcome_to_ezeats),
                getString(R.string.ezeats_is_your_destination_to_saving_on_your_favorite_restaurants),
                R.drawable.welcome_icon
            ),
            IntroSlide(
                getString(R.string.save_money_earn_rewards),
                getString(R.string.with_ezeats_you_can_save_money_get_special_offers_get_points_that_you_can_redeem_and_much_more),
                R.drawable.rewards_on_boarding
            ),
            IntroSlide(
                getString(R.string.location_access),
                getString(R.string.we_would_like_to_get_access_to_your_location_so_we_can_always_notify_you_about_the_best_near_deals),
                R.drawable.location_icon
            )));
    }

    /**
     * Initializes the introductory slider adapter and sets it to the ViewPager.
     * Also links the ViewPager to an indicator view.
     */
    private fun setupIntroSlider() {
        introSliderAdapter = IntroSliderAdapter(emptyList())
        binding.viewPager.adapter = introSliderAdapter
    }

    /**
     * Initializes the location utility for requesting location permissions.
     */
    private fun setupLocationUtil() {
        locationUtil = LocationUtil(this)
    }

    /**
     * Observes the ViewModel's introSlides LiveData and updates the adapter when data changes.
     */
    private fun setupObservers() {
        viewModel.introSlides.observe(viewLifecycleOwner) { slides ->
            introSliderAdapter.updateSlides(slides)
            binding.indicator.setViewPager(binding.viewPager)
        }
    }

    private fun changeStatusBackground() {
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.onboarding_background)
    }

    /**
     * Sets up the skip button to navigate to the intro screen when clicked.
     */
    private fun setupSkipButton() {
        binding.tvSkip.setOnClickListener {
            locationUtil?.askUserForLocationPermission(
                onPermissionGranted = { onLocationPermissionGranted() },
                onPermissionDenied = {  }
            )
        }
    }

    /**
     * Registers a page change callback to handle changes in the ViewPager's current page.
     * Updates the Next button text and click behavior based on the current page.
     */
    private fun setupPageChangeCallback() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateNextButton(position)
            }
        })
    }

    /**
     * Updates the Next button text and click behavior based on the current page position.
     * If on the last page, the button will request location permission.
     * Otherwise, it will navigate to the next page.
     *
     * @param position The current page position.
     */
    private fun updateNextButton(position: Int) {
        if (position == introSliderAdapter.itemCount - 1) {
            binding.buttonNext.text = getString(R.string.allow_finish)
            binding.buttonNext.setOnClickListener {
                locationUtil?.askUserForLocationPermission(
                    onPermissionGranted = { onLocationPermissionGranted() },
                    onPermissionDenied = {  }
                )
            }
        } else {
            binding.buttonNext.text = getString(R.string.next)
            binding.buttonNext.setOnClickListener {
                binding.viewPager.currentItem.let {
                    binding.viewPager.setCurrentItem(it + 1, false)
                }
            }
        }
    }

    /**
     * Navigates to the intro screen when location permission is granted.
     */
    private fun onLocationPermissionGranted() {
        getLocationAndAddress()
    }

    private fun getLocationAndAddress() {
        locationUtil?.getCurrentLocation(
            onLocationReceived = { latitude, longitude ->
                locationUtil?.reverseGeocode(latitude, longitude,
                    onAddressReceived = { address ->
                        viewModel.saveAddress(UserLocationAddress(address,latitude.toString(),longitude.toString()))
                    },
                    onError = { errorMessage ->
                        // Handle the error
                        Toasty.error(requireContext(), "Error: $errorMessage", Toast.LENGTH_LONG).show()
                    }
                )
            },
            onError = { errorMessage ->
                // Handle the error
                Toasty.error(requireContext(), "Error: $errorMessage", Toast.LENGTH_LONG).show()
            }
        )
        viewModel.userWatchOnBoarding(isWatched = true)
        navigateToIntroScreen()
    }

    /**
     * Navigates to the intro screen when location permission is denied.
     */
    private fun onLocationPermissionDenied() {
        viewModel.userWatchOnBoarding(isWatched = true)
        Log.d(TAG, "onLocationPermissionDenied: ")
        navigateToIntroScreen()
    }

    /**
     * Navigates to the intro screen.
     */
    private fun navigateToIntroScreen() {
        findNavController().navigate(R.id.action_onBoardingFragment_to_introScreenFragment)
    }

    /**
     * Handles the result of permission requests.
     *
     * @param requestCode The request code passed in requestPermissions().
     * @param permissions The requested permissions.
     * @param grantResults The grant results for the corresponding permissions.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                onLocationPermissionGranted()
            } else {
                onLocationPermissionDenied()
            }
        }
    }
}
