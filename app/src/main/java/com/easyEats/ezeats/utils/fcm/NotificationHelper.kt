package com.easyEats.ezeats.utils.fcm

import androidx.lifecycle.MutableLiveData

object NotificationHelper {
    val notificationLiveData = MutableLiveData<Map<String, String>>()

    fun sendNotificationToUI(data: Map<String, String>) {
        notificationLiveData.postValue(data)
    }
}
