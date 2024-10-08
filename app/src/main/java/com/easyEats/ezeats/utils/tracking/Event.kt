package com.easyEats.ezeats.utils.tracking

import com.easyEats.ezeats.MyApplication
import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject


object Event {
    fun sendEvent(buttonName: String, value: String, eventName: String) {
        val mixpanel = MixpanelAPI.getInstance(MyApplication.baseApplication, "your_mixpanel_token")
        val props = JSONObject()
        props.put(buttonName, value)
        mixpanel.track(eventName, props)
    }
}