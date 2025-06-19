package com.easyEats.ezeats.domain.main.model.response.compound


import com.google.gson.annotations.SerializedName

data class CompoundResponse(
    @SerializedName("result")
    var compoundResult: List<CompoundResult>? = listOf()
)