package com.easyEats.ezeats.ui.main.qr.permession

/**
 * Interface for handling permission requests and responses.
 * Implementations of this interface should define how to handle various states of permissions.
 */
interface PermissionHandler {
    /**
     * Handles the current state of a permission.
     *
     * @param permissionState The state of the permission to handle.
     */
    fun handlePermission(permissionState: PermissionState)
}
