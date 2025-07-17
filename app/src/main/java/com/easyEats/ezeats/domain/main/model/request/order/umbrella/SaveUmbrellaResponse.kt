package com.easyEats.ezeats.domain.main.model.request.order.umbrella


import com.google.gson.annotations.SerializedName

data class SaveUmbrellaResponse(
    @SerializedName("result")
    var result: Result? = Result()
)