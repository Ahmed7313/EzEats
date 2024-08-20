package com.easyEats.ezeats.utils.headers

import android.util.Log
import androidx.datastore.DataStore
import androidx.datastore.preferences.preferencesKey
import com.easyEats.ezeats.BuildConfig
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.data.local.data_store.DataStorePreference
import com.easyEats.ezeats.domain.auth.use_case.AuthUseCase
import com.easyEats.ezeats.utils.Constants
import com.easyEats.ezeats.utils.Constants.APPLICATION_ID
import com.easyEats.ezeats.utils.Constants.CUSTOM_SESSION_TOKEN
import com.easyEats.ezeats.utils.Constants.PARSE_KEY
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.util.prefs.Preferences
import javax.inject.Inject


/**
 * HeaderInterceptor is an OkHttp interceptor for adding Parse-specific headers to HTTP requests.
 * This interceptor adds the application ID and REST API key to each request.
 */
class HeaderInterceptor @Inject constructor(private val dataStorePreference: DataStorePreference) :
    Interceptor {

    @Inject
    lateinit var authUseCase: AuthUseCase
    override fun intercept(chain: Interceptor.Chain): Response {

        val token = runBlocking {
            runBlocking {
                dataStorePreference.fetchToken().first()
            }
        }
        Log.d(TAG, "intercept: $token")

        val request = chain.request().newBuilder()
            .addHeader(APPLICATION_ID, BuildConfig.PARSE_APPLICATION_ID)
            .addHeader(PARSE_KEY, BuildConfig.PARSE_REST_API_KEY)
            .addHeader(CUSTOM_SESSION_TOKEN, token?:"")
            .build()
        return chain.proceed(request)
    }
}
