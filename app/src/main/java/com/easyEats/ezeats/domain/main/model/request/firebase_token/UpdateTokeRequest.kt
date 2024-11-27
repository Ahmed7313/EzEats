package com.easyEats.ezeats.domain.main.model.request.firebase_token

data class UpdateTokeRequest(
    val deviceToken:String?,
    val deviceType:String="Android"
)
