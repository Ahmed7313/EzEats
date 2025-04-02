package com.easyEats.ezeats.ui.main.home.location

import android.app.Activity
import android.content.IntentSender
import android.util.Log
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
            // If location settings are satisfied, check location permission
            homeViewModel.checkLocationPermission()
        }

        task.addOnFailureListener { exception ->  // Handle failure in location settings
            when (exception) {
                is ResolvableApiException -> {
                    try {
                        // If resolvable, launch resolution intent
                        val intentSenderRequest =
                            IntentSenderRequest.Builder(exception.resolution).build()
                        locationSettingsLauncher.launch(intentSenderRequest)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Log the exception and show an error message
                        Toast.makeText(
                            context,
                            "Error: Unable to resolve location settings. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("Location", "Error launching location resolution", sendEx)
                    }
                }
                else -> {
                    // Handle non-resolvable issues and show a message to the user
                    Toast.makeText(
                        context,
                        "Error: Location services are required to continue.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("Location", "Non-resolvable location settings issue", exception)
                }
            }
        }
    }
}
