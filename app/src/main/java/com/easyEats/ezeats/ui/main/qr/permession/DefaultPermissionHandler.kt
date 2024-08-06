package com.easyEats.ezeats.ui.main.qr.permession

import com.easyEats.ezeats.ui.main.qr.QRScreenFragment
import javax.inject.Inject

class DefaultPermissionHandler @Inject constructor(private val fragment: QRScreenFragment) :
    PermissionHandler {
    override fun handlePermission(permissionState: PermissionState) {
        when (permissionState) {
            PermissionState.PERMISSION_GRANTED -> {
                fragment.setupScanner()
            }

            PermissionState.PERMISSION_NOT_GRANTED -> {
                fragment.requestCameraPermission()
            }
        }
    }
}