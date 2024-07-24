package com.easyEats.ezeats

import android.app.Application
import com.easyEats.ezeats.BuildConfig.APPLICATION_ID
import com.easyEats.ezeats.BuildConfig.CLIENT_KEY
import com.easyEats.ezeats.BuildConfig.URL
import com.parse.Parse
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        lateinit var baseApplication: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        baseApplication = this

        Parse.initialize(Parse.Configuration.Builder(this)
            .applicationId(APPLICATION_ID) // if defined
            .clientKey(CLIENT_KEY)
            .server(URL)
            .build()
        )
    }

}