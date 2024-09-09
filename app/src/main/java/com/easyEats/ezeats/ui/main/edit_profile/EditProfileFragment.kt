package com.easyEats.ezeats.ui.main.edit_profile


import android.net.Uri
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentEditProfileBinding
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>() {


    private val editProfileViewModel: EditProfileViewModel by viewModels()

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                editProfileViewModel.handleImageResult(it, requireContext().contentResolver)
                binding.ivProfile.setImageURI(uri)
            }
        }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentEditProfileBinding.inflate(layoutInflater)

    override fun setUpViews() {
        binding.viewModel = editProfileViewModel
        listenToObservers()

        binding.mcvEditProfile.setOnClickListener {
            pickImageFromGallery()
        }

        binding.ivProfile.setOnClickListener {
            pickImageFromGallery()
        }

        binding.tvEdit.setOnClickListener {
            binding.tvEdit.visibility = GONE
            binding.tvFullName.isEnabled = true
//            binding.tvEmail.isEnabled = true
//            binding.tilEmail.isEnabled = true
            binding.btnEditProfile.visibility = VISIBLE
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun listenToObservers() {
        editProfileViewModel.editUserResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    hideMainLoading()
                    Toasty.error(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }

                is Resource.Loading -> {
                    showMainLoading()
                }

                is Resource.Success -> {
                    findNavController().navigateUp()
                    hideMainLoading()
                }

                is Resource.UnAuthenticated -> {

                }
            }

        }
    }

    private fun pickImageFromGallery() {
        pickImageLauncher.launch("image/*")
    }


}