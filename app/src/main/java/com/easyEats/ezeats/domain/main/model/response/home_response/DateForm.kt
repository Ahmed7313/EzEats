package com.easyEats.ezeats.domain.main.model.response.home_response

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@OptIn(InternalSerializationApi::class)

data class DateForm(
    val iso: String?,
    val type: String?
)