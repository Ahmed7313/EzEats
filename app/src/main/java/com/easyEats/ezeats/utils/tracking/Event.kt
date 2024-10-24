package com.easyEats.ezeats.utils.tracking

import com.easyEats.ezeats.MyApplication
import com.easyEats.ezeats.utils.Constants.MIXPANEL_TOKEN
import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject


object Event {
    fun sendEvent(buttonName: String, value: String, eventName: String) {
        val mixpanel = MixpanelAPI.getInstance(MyApplication.baseApplication, MIXPANEL_TOKEN)
        val props = JSONObject()
        props.put(buttonName, value)
        mixpanel.track(eventName, props)
    }
}