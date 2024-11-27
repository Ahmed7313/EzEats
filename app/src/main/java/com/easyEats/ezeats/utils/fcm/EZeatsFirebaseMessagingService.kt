package com.easyEats.ezeats.utils.fcm


import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.ui.main.main.HomeActivity

class EZeatsFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Handle the received message
        val title = remoteMessage.notification?.title ?: "Default Title"
        val body = remoteMessage.notification?.body ?: "Default Body"

        // Extract data payload
        val offerId = remoteMessage.data["notificationType"]
        val restaurantId = remoteMessage.data["restaurantId"]
        Log.d(TAG, "onMessageReceived: $offerId")

        // Show notification
        showNotification(title, body, offerId,restaurantId)

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
    }

    private fun showNotification(title: String, message: String, offerId: String?,restaurantId:String?) {

        val notificationId = 100

        // Intent to open the specific screen
        val pendingIntent = sendIntent(offerId,restaurantId)

        // Build the notification
        val builder = buildNotification(title, message, pendingIntent)

        // Notification manager
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notification channel for Android 8.0+
        setNotificationChannelForSpecificAndroidVersions(notificationManager)

        // Notify the user
        notificationManager.notify(notificationId, builder.build())
    }

    private fun setNotificationChannelForSpecificAndroidVersions(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "my_channel_id", "My Channel", NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(title: String, message: String, pendingIntent: PendingIntent?): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(this, "my_channel_id")
            .setSmallIcon(R.drawable.easy_eats_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        return builder
    }

    private fun sendIntent(offerId: String?,restaurantId: String?): PendingIntent? {
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("notificationType", offerId)
            putExtra("restaurantId", restaurantId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return pendingIntent
    }
}