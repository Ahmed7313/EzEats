package com.easyEats.ezeats.utils.app_updates

import android.app.Activity
import android.widget.Toast
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class InAppUpdateManager(
    private val activity: Activity,
    private val requestCodeUpdate: Int = 100
) {
    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(activity)

    fun checkForUpdate() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val appUpdateInfo: AppUpdateInfo = appUpdateManager.appUpdateInfo.await()
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                ) {
                    startImmediateUpdate(appUpdateInfo)
                }
            } catch (e: Exception) {
//                Toast.makeText(activity, "Failed to check for updates.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startImmediateUpdate(appUpdateInfo: AppUpdateInfo) {
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            AppUpdateType.IMMEDIATE,
            activity,
            requestCodeUpdate
        )
    }

    fun handleUpdateResult(requestCode: Int, resultCode: Int) {
        if (requestCode == requestCodeUpdate) {
            if (resultCode != Activity.RESULT_OK) {
                // If the update is not completed, you can restart the update process or show a message
                Toast.makeText(activity, "Update failed! Please update the app to continue using it.", Toast.LENGTH_LONG).show()
                checkForUpdate() // Restart the update process
            }
        }
    }
}
