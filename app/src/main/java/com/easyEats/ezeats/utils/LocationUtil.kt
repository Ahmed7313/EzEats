package com.easyEats.ezeats.utils

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.easyEats.ezeats.R
import com.easyEats.ezeats.utils.Constants.LOCATION_PERMISSION_REQUEST_CODE

/**
 * Utility class to handle location permission requests within a Fragment.
 *
 * @property fragment The fragment from which the permission request is initiated.
 */
class LocationUtil(private val fragment: Fragment) {

    /**
     * Requests location permission from the user.
     *
     * @param onPermissionGranted Callback invoked when the location permission is granted.
     * @param onPermissionDenied Callback invoked when the location permission is denied.
     */
    fun askUserForLocationPermission(
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit
    ) {
        when {
            // If permission is already granted, invoke the granted callback
            ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                onPermissionGranted()
            }
            // If rationale should be shown, show the rationale dialog
            fragment.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                showPermissionRationaleDialog()
            }
            // Request permission directly if rationale is not needed
            else -> {
                fragment.requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    /**
     * Displays a dialog explaining why the location permission is needed.
     * If the user agrees, the permission request is re-initiated.
     */
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
