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
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileScreenFragment : BaseFragment<FragmentProfileScreenBinding>(),
    DeleteAccountDialogFragment.DialogCallback {

    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var user: UserInfoResponse

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentProfileScreenBinding.inflate(layoutInflater)

    override fun onResume() {
        super.onResume()
        profileViewModel.getUserInfoResult()
    }

    override fun setUpViews() {
        listenToObservers()
        binding.tvProfile.setOnClickListener {
            if (::user.isInitialized)
                findNavController().navigate(ProfileScreenFragmentDirections.goToEditProfile(user))
        }
        binding.tvLogin.setOnClickListener {
            val intent = Intent(requireActivity(), AuthActivity::class.java)
            intent.putExtra("FRAGMENT_ID", R.id.signInScreenFragment)
            startActivity(intent)
        }
        binding.tvLogout.setOnClickListener {
            profileViewModel.clearDeviceTokenResult()
        }

        binding.tvDeleteAccount.setOnClickListener {
            val dialog = DeleteAccountDialogFragment()
            dialog.setDialogCallback(this)
            dialog.show(parentFragmentManager, "CustomDialog")
        }

        binding.tvHelp.setOnClickListener {
            openBrowser("https://www.ez-eats.com/privacypolicy")
        }

        binding.tvPromoCode.setOnClickListener {
            findNavController().navigate(R.id.goToVoucherScreen)
        }

        binding.tvContactUs.setOnClickListener {
            contactUs()
        }

        binding.tvInstagram.setOnClickListener {
            openBrowser("https://www.instagram.com/ezeats.eg/?igsh=MXN1OW9nM2R1Zjd0eg%3D%3D")
        }

        binding.tvOrderHistory.setOnClickListener {
            findNavController().navigate(R.id.goToOrderHistoryScreen)
        }

    }

    fun listenToObservers() {
        profileViewModel.clearDeviceTokenLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    hideMainLoading()
                }

                is Resource.Loading -> {
                    showMainLoading()
                }

                is Resource.Success -> {
                    hideMainLoading()
                    profileViewModel.saveToke("")
                    findNavController().navigate(R.id.homeScreenFragment)
                }

                is Resource.UnAuthenticated -> {}
            }

        }
        profileViewModel.userResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    hideMainLoading()
                }

                is Resource.Loading -> {
                    showMainLoading()
                }

                is Resource.Success -> {
                    hideMainLoading()
                    user = resource.data?.result?.data!!
                    binding.userInfo = user
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
        profileViewModel.deleteUserResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }

                is Resource.Success -> {
                    profileViewModel.saveToke("")
                    profileViewModel.setUserVerification(false)
                    startActivity(Intent(requireActivity(), AuthActivity::class.java))
                }

                is Resource.UnAuthenticated -> {

                }
            }

        }
        lifecycleScope.launch {
            profileViewModel.getToken().asLiveData()
                .observe(viewLifecycleOwner) { token ->
                    if (token.isNullOrEmpty()) {
                        binding.containerHeader.visibility = GONE
                        binding.llGuestUser.visibility = VISIBLE
                        binding.llAccount.visibility = GONE
                    }
                }
        }
    }

    override fun onDeleteClicked() {
        profileViewModel.deleteUserInfoResult()
    }

    override fun onCancelClicked() {}


    private fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        requireActivity().startActivity(intent)
    }

    private fun contactUs() {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:+201040302211")
        requireActivity().startActivity(dialIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideMainLoading()
    }
}