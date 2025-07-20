package com.easyEats.ezeats.domain.main.model.response.payment.savedCard.response


import com.google.gson.annotations.SerializedName

data class FetchSavedCardResponse(
    @SerializedName("result")
    var result: Result? = Result()
)