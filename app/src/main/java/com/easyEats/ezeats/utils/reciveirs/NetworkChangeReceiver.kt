package com.easyEats.ezeats.utils.reciveirs

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

/**
 * NetworkChangeReceiver is a BroadcastReceiver that listens for network connectivity changes.
 * It triggers a callback with the current network connectivity status.
 *
 * @param callback A function that will be called with the connectivity status: true if connected, false otherwise.
 */
class NetworkChangeReceiver(private val callback: (Boolean) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val connectivityManager = it.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            val isConnected = networkInfo?.isConnected == true
            callback(isConnected)
        }
    }
}