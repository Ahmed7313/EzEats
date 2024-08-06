package com.easyEats.ezeats.utils.location

import com.easyEats.ezeats.utils.Constants.LOCATION_PERMISSION_REQUEST_CODE
import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.easyEats.ezeats.R
import com.google.android.gms.location.*

class LocationUtil(private val fragment: Fragment) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(fragment.requireContext())
    private val geocoder: Geocoder = Geocoder(fragment.requireContext())

    fun askUserForLocationPermission(
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit
    ) {
        when {
            ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                onPermissionGranted()
            }

            fragment.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                showPermissionRationaleDialog()
            }

            else -> {
                fragment.requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    fun getCurrentLocation(
        onLocationReceived: (Double, Double) -> Unit,
        onError: (String) -> Unit
    ) {
        if (ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        onLocationReceived(it.latitude, it.longitude)
                    } ?: run {
                        onError("Location not available")
                    }
                }
                .addOnFailureListener {
                    onError("Failed to get location")
                }
        } else {
            onError("Location permission not granted")
        }
    }

    fun reverseGeocode(
        latitude: Double,
        longitude: Double,
        onAddressReceived: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses?.isNotEmpty() == true) {
                val address: Address = addresses[0]
                val addressText = StringBuilder()
                for (i in 0..address.maxAddressLineIndex) {
                    addressText.append(address.getAddressLine(i)).append("\n")
                }
                onAddressReceived(addressText.toString())
            } else {
                onError("No address found")
            }
        } catch (e: Exception) {
            onError("Geocoder service not available")
        }
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(fragment.requireContext())
            .setTitle(fragment.getString(R.string.location_permission_required))
            .setMessage(fragment.getString(R.string.location_permission_is_required_to_use_this_feature_please_grant_the_permission_in_the_app_settings))
            .setPositiveButton(fragment.getString(R.string.ok)) { _, _ ->
                fragment.requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
            .setNegativeButton(fragment.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

}
