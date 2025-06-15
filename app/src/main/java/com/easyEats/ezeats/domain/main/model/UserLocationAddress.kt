package com.easyEats.ezeats.domain.main.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserLocationAddress(val address:String?,val lat:String?,val lng:String?):Parcelable
