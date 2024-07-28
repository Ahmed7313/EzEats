package com.easyEats.ezeats

import android.app.Application
import com.easyEats.ezeats.BuildConfig.APPLICATION_ID
import com.easyEats.ezeats.BuildConfig.CLIENT_KEY
import com.easyEats.ezeats.BuildConfig.URL
import com.easyEats.ezeats.utils.Constants.EASY_EATS_LOGS
import com.parse.Parse
import dagger.hilt.android.HiltAndroidApp


/**
 * MyApplication is the main application class for initializing Hilt and Parse SDK.
 * This class is annotated with @HiltAndroidApp to trigger Hilt's code generation.
 */
@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        /**
         * A static reference to the application instance.
         * It is used to access the application context from other parts of the app.
         */
        lateinit var baseApplication: MyApplication
            private set

        lateinit var TAG: String
            private set
    }

    /**
     * Initializes the application and sets up the Parse SDK.
     */
    override fun onCreate() {
        super.onCreate()

        baseApplication = this
        TAG = EASY_EATS_LOGS

        // Initialize Parse with the necessary configuration
        Parse.initialize(Parse.Configuration.Builder(this)
            .applicationId(APPLICATION_ID)
            .clientKey(CLIENT_KEY)
            .server(URL)
            .build()
        )
    }
}
