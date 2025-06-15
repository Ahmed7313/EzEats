package com.easyEats.ezeats.ui.main.home.notifications

import android.app.Activity
import android.util.Log
import androidx.navigation.NavController
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.request.firebase_token.UpdateTokeRequest
import com.easyEats.ezeats.ui.main.home.HomeScreenFragmentDirections
import com.easyEats.ezeats.ui.main.home.HomeViewModel
import com.google.firebase.messaging.FirebaseMessaging

data class Notification(
    private val homeViewModel: HomeViewModel,
    private val context: Activity,
    private val navController: NavController
) {

    fun pushNotification(hasReceivedNotification:()->Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                hasReceivedNotification()
                Log.d(TAG, "pushNotification: $token")
            } else {
                Log.e("FCM", "Fetching FCM token failed", task.exception)
            }
        }
    }

    fun navigateToScreen(notification: String?) {
        when (notification) {
            "order status update" -> {
                val action =
                    HomeScreenFragmentDirections.goToActiveOrderDetails()
                navController.navigate(action)
            }

            "PromoCode" -> {
                navController.navigate(R.id.goToVoucherScreen)
            }

            "TrackingActiveOrder" -> {
                navController.navigate(R.id.goToActiveOrderDetails)
            }

            else -> {
                Log.d("Navigation Kenawy", "Unknown screen: $notification")
            }
        }
    }

    fun refreshFirebaseToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@addOnCompleteListener
                }

                // Get new FCM token
                val token = task.result
                Log.d(TAG, "Refreshed token: $token")

                sendTokenToServer(token)
            }
    }

    private fun sendTokenToServer(token: String?) {
        homeViewModel.updateDeviceToken(UpdateTokeRequest(token))
    }
}