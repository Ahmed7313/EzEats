package com.easyEats.ezeats.ui.main.qr.permession

import com.easyEats.ezeats.ui.main.qr.QRScreenFragment
import javax.inject.Inject

/**
 * Default implementation of the PermissionHandler interface for handling camera permissions.
 * This class handles the camera permission state by either setting up the scanner or requesting
 * the camera permission from the user.
 *
 * @param fragment The QRScreenFragment instance that this handler will interact with.
 */
class DefaultPermissionHandler @Inject constructor(private val fragment: QRScreenFragment) :
    PermissionHandler {

    /**
     * Handles the camera permission state and performs actions based on whether the permission
     * is granted or not.
     *
     * @param permissionState The current state of the camera permission.
     */
    override fun handlePermission(permissionState: PermissionState) {
        when (permissionState) {
            PermissionState.PERMISSION_GRANTED -> {
                // If the permission is granted, set up the scanner
                fragment.setupScanner()
            }

            PermissionState.PERMISSION_NOT_GRANTED -> {
                // If the permission is not granted, request the camera permission from the user
                fragment.requestCameraPermission()
            }
        }
    }
}
