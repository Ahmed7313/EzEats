package com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.result


import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("currentPage")
    var currentPage: Int? = 0,
    @SerializedName("recordsPerPage")
    var recordsPerPage: Int? = 0,
    @SerializedName("totalPages")
    var totalPages: Int? = 0,
    @SerializedName("totalRecords")
    var totalRecords: Int? = 0
)