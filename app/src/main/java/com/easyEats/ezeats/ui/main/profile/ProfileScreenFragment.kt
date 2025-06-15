package com.easyEats.ezeats.ui.main.profile


import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentProfileScreenBinding
import com.easyEats.ezeats.domain.main.model.response.user_info.UserInfoResponse
import com.easyEats.ezeats.ui.auth.main.AuthActivity
import com.easyEats.ezeats.ui.main.delete_account.DeleteAccountDialogFragment
import com.easyEats.ezeats.ui.main.profile.logout.LogOutFragment
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Fragment representing the profile screen in the application.
 * It handles user profile information, logout functionality, navigation to other screens,
 * and provides links to external resources like privacy policy and social media.
 * This fragment is injected with dependencies using `@AndroidEntryPoint`.
 */
@AndroidEntryPoint
class ProfileScreenFragment : BaseFragment<FragmentProfileScreenBinding>(), LogOutFragment.DialogCallback {

    // ViewModel for handling profile data
    private val profileViewModel: ProfileViewModel by viewModels()

    // User information object that will be populated after fetching user data
    private lateinit var user: UserInfoResponse

    /**
     * Inflates the layout and returns the binding instance for this fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate the layout.
     * @param container The parent ViewGroup that this fragment's UI should be attached to.
     * @return The inflated binding object for the fragment.
     */
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentProfileScreenBinding.inflate(layoutInflater)

    /**
     * Fetches the user information when the fragment is resumed.
     */
    override fun onResume() {
        super.onResume()
        profileViewModel.getUserInfoResult()  // Get user info from ViewModel
    }

    /**
     * Sets up the views and handles click events for various UI elements.
     */
    override fun setUpViews() {
        // Navigate to the edit profile screen when the profile is clicked
        binding.tvProfile.setOnClickListener {
            if (::user.isInitialized)
                findNavController().navigate(ProfileScreenFragmentDirections.goToEditProfile(user))
        }

        // Navigate to the login screen when the login option is clicked
        binding.tvLogin.setOnClickListener {
            val intent = Intent(requireActivity(), AuthActivity::class.java)
            intent.putExtra("FRAGMENT_ID", R.id.signInScreenFragment)
            startActivity(intent)
        }

        // Show the logout dialog when the logout icon is clicked
        binding.ivLogout.setOnClickListener {
            val dialog = LogOutFragment()
            dialog.setDialogCallback(this)  // Set callback for the dialog
            dialog.show(parentFragmentManager, "CustomDialog")
        }

        // Open the privacy policy URL in a browser when the help option is clicked
        binding.tvHelp.setOnClickListener {
            openBrowser("https://www.ez-eats.com/privacypolicy")
        }

        binding.tvCart.setOnClickListener {
            findNavController().navigate(R.id.goToCartScreen)
        }

        // Navigate to the voucher screen when the promo code option is clicked
        binding.tvPromoCode.setOnClickListener {
            findNavController().navigate(R.id.goToVoucherScreen)
        }

        // Call the contact us phone number when the contact us option is clicked
        binding.tvContactUs.setOnClickListener {
            contactUs()
        }

        // Open Instagram in a browser when the Instagram option is clicked
        binding.tvInstagram.setOnClickListener {
            openBrowser("https://www.instagram.com/ezeats.eg/?igsh=MXN1OW9nM2R1Zjd0eg%3D%3D")
        }

        // Navigate to the order history screen when the order history option is clicked
        binding.tvOrderHistory.setOnClickListener {
            findNavController().navigate(R.id.goToOrderHistoryScreen)
        }
    }

    /**
     * Listens to the LiveData observers to handle different states of the data,
     * such as user data fetching and logout process.
     */
    override fun listenToObservers() {
        // Observe the logout result
        profileViewModel.clearDeviceTokenLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> hideMainLoading()  // Hide loading on error
                is Resource.Loading -> showMainLoading()  // Show loading when processing
                is Resource.Success -> {
                    hideMainLoading()  // Hide loading on success
                    profileViewModel.saveToke("")  // Clear token
                    findNavController().navigate(R.id.homeScreenFragment)  // Navigate to home screen
                }
                is Resource.UnAuthenticated -> {}
            }
        }

        // Observe the user data result
        profileViewModel.userResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> hideMainLoading()
                is Resource.Loading -> showMainLoading()
                is Resource.Success -> {
                    hideMainLoading()
                    user = resource.data?.result?.data!!  // Save the user data
                    binding.userInfo = user  // Bind the user info to the view
                }
                is Resource.UnAuthenticated -> {
                    hideMainLoading()
                    profileViewModel.saveToke("")
                    binding.containerHeader.visibility = GONE
                    binding.llGuestUser.visibility = VISIBLE
                    binding.llAccount.visibility = GONE
                }
            }
        }

        // Observe the token and update UI visibility accordingly
        lifecycleScope.launch {
            profileViewModel.getToken().asLiveData().observe(viewLifecycleOwner) { token ->
                if (token.isNullOrEmpty()) {
                    binding.containerHeader.visibility = GONE
                    binding.llGuestUser.visibility = VISIBLE
                    binding.llAccount.visibility = GONE
                }
            }
        }
    }

    /**
     * Handles the logout process by clearing the device token.
     */
    override fun onLogOutClicked() {
        profileViewModel.clearDeviceTokenResult()  // Clear the device token
    }

    /**
     * Handles the cancel action from the logout dialog.
     */
    override fun onCancelClicked() {}

    /**
     * Opens the provided URL in the browser.
     *
     * @param url The URL to open.
     */
    private fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        requireActivity().startActivity(intent)
    }

    /**
     * Initiates a phone call to the specified contact number.
     */
    private fun contactUs() {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse(getString(R.string.EZeats_Phone_Number))
        requireActivity().startActivity(dialIntent)
    }

    /**
     * Hides the loading spinner when the view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        hideMainLoading()
    }

    /**
     * Hides the loading spinner when the fragment is paused.
     */
    override fun onPause() {
        super.onPause()
        hideMainLoading()
    }
}
