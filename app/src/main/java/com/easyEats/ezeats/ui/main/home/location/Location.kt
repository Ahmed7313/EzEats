package com.easyEats.ezeats.ui.main.home.location

import android.app.Activity
import android.content.IntentSender
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.easyEats.ezeats.ui.main.home.HomeViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority

data class Location(
    private val context: Activity,
    private val homeViewModel: HomeViewModel,
    private val locationSettingsLauncher: ActivityResultLauncher<IntentSenderRequest>
) {
    // Requests the user to enable location services if not enabled
     fun askUserToEnableLocation() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 1000L
        ).build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(context)
        val task = client.checkLocationSettings(builder.build())  // Check location settings

        task.addOnSuccessListener {
            homeViewModel.checkLocationPermission()  // Check location permission if settings are correct
        }

        task.addOnFailureListener { exception ->  // Handle failure in location settings
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution).build()
                    locationSettingsLauncher.launch(intentSenderRequest)  // Launch resolution intent
                } catch (sendEx: IntentSender.SendIntentException) {
                    Toast.makeText(
                        context,
                        "Error: ${sendEx.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}