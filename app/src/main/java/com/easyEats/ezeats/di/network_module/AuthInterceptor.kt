package com.easyEats.ezeats.di.network_module

import android.content.Context
import android.content.Intent
import android.util.Log
import com.easyEats.ezeats.data.local.data_store.DataStorePreference
import com.easyEats.ezeats.ui.auth.main.AuthActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import kotlin.jvm.java
import kotlin.or

class AuthInterceptor(
    private val dataStorePreference: DataStorePreference,
    private val context: Context
) : Interceptor {
    val TAG = "AuthInterceptor"
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        Log.d(TAG, "intercept: ${response.code}")
        when (response.code) {
            400 -> {
                // Clear user session
                GlobalScope.launch(Dispatchers.Main) {
                    dataStorePreference.clearUserData()

                    // Navigate to login
                    val intent = Intent(context, AuthActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)
                }

                // Start login activity

            }
        }
        return response
    }
}