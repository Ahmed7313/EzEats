package com.easyEats.ezeats.ui.compose.map.mapUtils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import com.easyEats.ezeats.ui.main.main.HomeActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.util.Locale


class GetLocation(var context: Context) {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val TAG = "GetLocation"

    init {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    }
    @SuppressLint("MissingPermission")
    fun getLastUserLocation(
        onGetLastLocationSuccess: (Pair<Double, Double>) -> Unit,
        onGetLastLocationFailed: (Exception) -> Unit,
        onGetLastLocationIsNull: () -> Unit
    ) {
        Log.d(TAG, "getLastUserLocation: ")



        // Check if location permissions are granted
        if (areLocationPermissionsGranted()) {
            Log.d(TAG, "areLocationPermissionsGranted: ")

            // Retrieve the last known location
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    Log.d(TAG, "location: ")
                    location?.let {
                        // If location is not null, invoke the success callback with latitude and longitude
                        onGetLastLocationSuccess(Pair(it.latitude, it.longitude))
                    }?.run {
                        onGetLastLocationIsNull()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "location: exception "+exception.message)

                    // If an error occurs, invoke the failure callback with the exception
                    onGetLastLocationFailed(exception)
                }


            Log.d(TAG, "getLastUserLocation: start another fused")
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context as HomeActivity)
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                10_000L
            ).build()
            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

            val settingsClient = LocationServices.getSettingsClient(context)

            settingsClient.checkLocationSettings(builder.build())
                .addOnSuccessListener {
                    // Location settings are satisfied, proceed with getCurrentLocation()
                    fusedLocationProviderClient.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        CancellationTokenSource().token
                    ).addOnSuccessListener { location ->
                        location?.let {
                            // Use the location data
                            Log.d("Location", "Lat: ${it.latitude}, Lng: ${it.longitude}")
                            onGetLastLocationSuccess(Pair(it.latitude, it.longitude))

                        }
                    }.addOnFailureListener { exception ->
                        // Handle exception
                        Log.e("Location", "Error getting location: ${exception.message}")
                        onGetLastLocationFailed(exception)

                    }
                }
                .addOnFailureListener { exception ->
                    // Location settings are not satisfied, prompt user to change settings
                    if (exception is ResolvableApiException) {
                        try {
                            Log.d(TAG, "getLastUserLocation: chek settings "+exception.message)
                            exception.startResolutionForResult(context as HomeActivity, 1001)

                        } catch (sendEx: IntentSender.SendIntentException) {
                            // Handle the error
                        }
                    }
                }


        }
    }


    /**
     * Retrieves the current user location asynchronously.
     *
     * @param onGetCurrentLocationSuccess Callback function invoked when the current location is successfully retrieved.
     *        It provides a Pair representing latitude and longitude.
     * @param onGetCurrentLocationFailed Callback function invoked when an error occurs while retrieving the current location.
     *        It provides the Exception that occurred.
     * @param priority Indicates the desired accuracy of the location retrieval. Default is high accuracy.
     *        If set to false, it uses balanced power accuracy.
     */
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        onGetCurrentLocationSuccess: (Pair<Double, Double>) -> Unit,
        onGetCurrentLocationFailed: (Exception) -> Unit,
        priority: Boolean = true
    ) {
        // Determine the accuracy priority based on the 'priority' parameter
        val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
        else Priority.PRIORITY_BALANCED_POWER_ACCURACY

        // Check if location permissions are granted
        if (areLocationPermissionsGranted()) {
            Log.d(TAG, "getCurrentLocation: granted")
            // Retrieve the current location asynchronously
            fusedLocationProviderClient.getCurrentLocation(
                accuracy, CancellationTokenSource().token,
            ).addOnSuccessListener { location ->
                Log.d(TAG, "getCurrentLocation: current location")
                if(location == null){
                    onGetCurrentLocationFailed(java.lang.Exception("error in fetch location"))
                }
                location?.let {
                    Log.d(TAG, "getCurrentLocation: let")
                    // If location is not null, invoke the success callback with latitude and longitude
                    onGetCurrentLocationSuccess(Pair(it.latitude, it.longitude))
                }?.run {
                    //Location null do something
                    Log.d(TAG, "getCurrentLocation: null")
                }
            }.addOnFailureListener { exception ->
                Log.d(TAG, "getCurrentLocation: error location "+exception.message)
                // If an error occurs, invoke the failure callback with the exception
                onGetCurrentLocationFailed(exception)
            }
        }
    }


    /**
     * Checks if location permissions are granted.
     *
     * @return true if both ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION permissions are granted; false otherwise.
     */
    private fun areLocationPermissionsGranted(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED)
    }
    fun getAddressFromLatLng(context: Context, latitude: Double, longitude: Double,address: (String) -> Unit) {
        try {
            val geocoder = Geocoder(context, Locale.ENGLISH)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(latitude, longitude, 1,object : Geocoder.GeocodeListener{
                    override fun onGeocode(res: MutableList<Address>) {
                        if(res.isNotEmpty()){
                            address(res[0].countryName + " " + res[0].locality + " "+ res[0].getAddressLine(0).toString())
                        }else{
                            address("")
                        }

                    }

                    override fun onError(errorMessage: String?) {
                        super.onError(errorMessage)
                        address("")
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
            address("")
        }
    }

}

