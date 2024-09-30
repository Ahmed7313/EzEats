package com.easyEats.ezeats.ui.main.edit_profile


import android.app.DatePickerDialog
import android.net.Uri
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentEditProfileBinding
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
        binding.ivProfile.isEnabled = false
        binding.tvBirthDate.isEnabled = false
        binding.mcvEditProfile.isEnabled = false
        binding.mcvEditProfile.visibility = GONE
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
            binding.ivProfile.isEnabled = true
            binding.tvBirthDate.isEnabled = true
            binding.mcvEditProfile.isEnabled = true
            binding.btnEditProfile.visibility = VISIBLE
            binding.mcvEditProfile.visibility = VISIBLE
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tvBirthDate.setOnClickListener {

            // Get the current date to set as default for the DatePicker
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Create and show a DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                    // Update EditText with the selected date
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay)
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                    binding.tvBirthDate.setText(dateFormat.format(selectedDate.time))
                    editProfileViewModel.birthDate = dateFormat.format(selectedDate.time)
                },
                year, month, day
            )
            datePickerDialog.show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        hideMainLoading()
    }


}