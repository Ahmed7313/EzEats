package com.easyEats.ezeats.ui.auth.on_boarding

import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentOnBoardingBinding
import com.easyEats.ezeats.utils.Constants.LOCATION_PERMISSION_REQUEST_CODE
import com.easyEats.ezeats.utils.LocationUtil
import dagger.hilt.android.AndroidEntryPoint

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
    private lateinit var locationUtil: LocationUtil

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
            navigateToIntroScreen()
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
                locationUtil.askUserForLocationPermission(
                    onPermissionGranted = { onLocationPermissionGranted() },
                    onPermissionDenied = { onLocationPermissionDenied() }
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
        viewModel.userWatchOnBoarding(isWatched = true)
        navigateToIntroScreen()
    }

    /**
     * Navigates to the intro screen when location permission is denied.
     */
    private fun onLocationPermissionDenied() {
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
