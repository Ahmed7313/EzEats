package com.easyEats.ezeats.domain.main.model.response.home_response

import kotlinx.serialization.Serializable

@Serializable

data class DateForm(
    val iso: String?,
    val type: String?
)