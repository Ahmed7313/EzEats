package com.easyEats.ezeats.ui.main.location

import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.FragmentLocationBottomSheetDialogBinding
import com.easyEats.ezeats.domain.main.model.UserLocationAddress
import com.easyEats.ezeats.domain.main.model.response.location_response.Location
import com.easyEats.ezeats.ui.main.location.adapter.LocationAdapter
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.utils.Resource
import com.easyEats.ezeats.utils.location.LocationUtil
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationBottomSheetDialogFragment : BottomSheetDialogFragment(),
    LocationAdapter.OnLocationListener {

    val mainViewModel: MainViewModel by activityViewModels()
    private val locationViewModel: LocationViewModel by viewModels()
    private lateinit var binding: FragmentLocationBottomSheetDialogBinding
    private lateinit var locationAdapter: LocationAdapter

    // Register ActivityResultLauncher
    private lateinit var locationSettingsLauncher: ActivityResultLauncher<IntentSenderRequest>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationBottomSheetDialogBinding.inflate(layoutInflater)
        locationViewModel.setLocationUtil(LocationUtil(this))

        locationSettingsLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) {
            locationViewModel.checkLocationPermission()
        }
        locationAdapter = LocationAdapter(this)
        binding.rvLocation.adapter = locationAdapter
        binding.tvRefresh.setOnClickListener { askUserToEnableLocation() }

        listenToObservers()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        locationViewModel.getLocation()
    }

    private fun listenToObservers() {
        locationViewModel.getLocationsResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {

                }

                is Resource.Loading -> {
                    binding.loader.visibility = VISIBLE
                }

                is Resource.Success -> {
                    binding.loader.visibility = GONE

                    locationAdapter.submitList(resource.data?.result?.data)
                }

                is Resource.UnAuthenticated -> {}
                else ->{}

            }
        }
        lifecycleScope.launch {
            mainViewModel.fetchCurrentAddress().collect {
                it?.let {
                    binding.tvTitleLocation.text = it.address ?: ""
                }
            }
        }
    }

    override fun onLocationClicked(location: Location) {
        // Update the location name in the TextView
        binding.tvTitleLocation.text = location.name

        // Call the ViewModel's saveAddress function
        mainViewModel.saveAddress(
            UserLocationAddress(
                location.name,
                location.geoPoint?.latitude.toString(),
                location.geoPoint?.longitude.toString()
            )
        )

        // Observe when the saving is done and then dismiss the dialog
        mainViewModel.saveAddressResult.observe(viewLifecycleOwner) { isSaved ->
            if (isSaved) {
                lifecycleScope.launch {
                    dismiss() // Dismiss the dialog when save is successful
                }
            }
        }
    }

    private fun askUserToEnableLocation() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 1000L
        ).build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(requireActivity())
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            locationViewModel.checkLocationPermission()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution).build()
                    locationSettingsLauncher.launch(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Toast.makeText(requireContext(), "Error: ${sendEx.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


}