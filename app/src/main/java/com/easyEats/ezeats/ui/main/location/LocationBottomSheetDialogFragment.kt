package com.easyEats.ezeats.ui.main.location

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.FragmentLocationBottomSheetDialogBinding
import com.easyEats.ezeats.domain.main.model.UserLocationAddress
import com.easyEats.ezeats.domain.main.model.response.location_response.Location
import com.easyEats.ezeats.ui.main.location.adapter.LocationAdapter
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationBottomSheetDialogFragment : BottomSheetDialogFragment(),
    LocationAdapter.OnLocationListener {

    val mainViewModel: MainViewModel by activityViewModels()
    private val locationViewModel: LocationViewModel by viewModels()
    private lateinit var binding: FragmentLocationBottomSheetDialogBinding
    private lateinit var locationAdapter: LocationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationBottomSheetDialogBinding.inflate(layoutInflater)

        locationAdapter = LocationAdapter(this)
        binding.rvLocation.adapter = locationAdapter
        binding.tvRefresh.setOnClickListener { locationViewModel.getLocation() }

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
                    binding.loader.visibility=VISIBLE
                }
                is Resource.Success -> {
                    binding.loader.visibility= GONE

                    locationAdapter.submitList(resource.data?.result?.data)
                }

                is Resource.UnAuthenticated -> {}
            }
        }
    }

    override fun onLocationClicked(location: Location) {
        binding.tvTitleLocation.text = location.name
        mainViewModel.saveAddress(
            UserLocationAddress(
                location.name,
                location.geoPoint?.latitude.toString(),
                location.geoPoint?.longitude.toString()
            )
        )
        dismiss()
    }


}