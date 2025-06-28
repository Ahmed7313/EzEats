package com.easyEats.ezeats.ui.main.edit_profile

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentEditProfileBinding
import com.easyEats.ezeats.ui.auth.main.AuthActivity
import com.easyEats.ezeats.ui.main.delete_account.DeleteAccountDialogFragment
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


/**
 * EditProfileFragment handles the editing of user profile information such as profile picture and birthdate.
 * It uses the EditProfileViewModel for data binding and observing changes in the profile update state.
 *
 * This fragment includes functionality for selecting an image from the gallery, updating the birthdate, and saving profile changes.
 */
@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(), DeleteAccountDialogFragment.DialogCallback {

    // ViewModel to manage the data and state related to editing user profile
    private val editProfileViewModel: EditProfileViewModel by viewModels()

    private var initialFullName: String = ""


    // ActivityResultLauncher to handle image picking from the gallery
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // Converts the selected image URI to a Base64 encoded string and updates ViewModel
                val base64Image = convertUriToBase64(it)
                editProfileViewModel.setImageBase64(base64Image)
                binding.ivProfile.setImageURI(uri)
                editProfileViewModel.isSavedButtonEnabled.value = true
            }
        }

    /**
     * Inflate the Fragment's layout using the FragmentEditProfileBinding.
     */
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentEditProfileBinding.inflate(layoutInflater)

    /**
     * Initialize the views and set up event listeners.
     */
    override fun setUpViews() {
        binding.viewModel = editProfileViewModel
//        listenToObservers()

        // Opens image picker when profile or edit profile views are clicked
        binding.mcvEditProfile.setOnClickListener { pickImageFromGallery() }
        binding.ivProfile.setOnClickListener {
            pickImageFromGallery()
        }

        binding.tvDeleteAccount.setOnClickListener {
            val dialog = DeleteAccountDialogFragment()
            dialog.setDialogCallback(this)
            dialog.show(parentFragmentManager, "CustomDialog")
        }


        // Navigates back on back button click
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }

        // Opens DatePickerDialog to select birthdate
        binding.tvBirthDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay)
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                    binding.tvBirthDate.setText(dateFormat.format(selectedDate.time))
                    editProfileViewModel.birthDate = dateFormat.format(selectedDate.time)
                    editProfileViewModel.isSavedButtonEnabled.value = true
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        initialFullName = editProfileViewModel.fullName ?: ""
        binding.tvFullName.doOnTextChanged { text, _, _, _ ->
            // Compare the current text with the initial value
            editProfileViewModel.isSavedButtonEnabled.value = text.toString() != initialFullName
            Log.d(TAG, "setUpViews: text ${text.toString()}")
            Log.d(TAG, "setUpViews: initial $initialFullName")
        }
    }

    /**
     * Observes the LiveData from the ViewModel to handle edit user result.
     */
    override fun listenToObservers() {
        editProfileViewModel.editUserResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    hideMainLoading()
                    Toasty.error(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }

                is Resource.Loading -> showMainLoading()
                is Resource.Success -> {
                    findNavController().navigateUp()
                    hideMainLoading()
                }

                is Resource.UnAuthenticated -> { /* handle unauthenticated state */
                }
                else ->{}

            }
        }

        editProfileViewModel.deleteUserResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }

                is Resource.Success -> {
                    editProfileViewModel.saveToke("")
                    editProfileViewModel.setUserVerification(false)
                    startActivity(Intent(requireActivity(), AuthActivity::class.java))
                }

                is Resource.UnAuthenticated -> {

                }
                else ->{}

            }

        }
    }

    /**
     * Launches the image picker to select an image from the gallery.
     */
    private fun pickImageFromGallery() {
        pickImageLauncher.launch("image/*")
    }

    /**
     * Converts a URI to a Base64-encoded string by first decoding the URI into a Bitmap, compressing the Bitmap,
     * and encoding it as a Base64 string.
     *
     * @param uri The URI of the image to convert
     * @return A Base64-encoded string representation of the image
     */
    private fun convertUriToBase64(uri: Uri): String {
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    /**
     * Clears any loading indicators when the view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        hideMainLoading()
    }

    override fun onDeleteClicked() {
        editProfileViewModel.deleteUserInfoResult()
    }

    override fun onCancelClicked() {
    }
}
