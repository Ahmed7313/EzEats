package com.easyEats.ezeats.domain.main.model.response.user_info

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfoResponse(
    val name: String?,
    val username: String?,
    val email: String?,
    val profilePicture: ProfilePicture?,
    val mobileNumber: String?,
    val birthDate: String?,
) : Parcelable {
    fun getFirstName(fullName: String?): String? {
        return fullName?.split(" ")?.firstOrNull() ?: ""
    }

    fun getRemainingName(fullName: String?): String {
        val parts = fullName?.split(" ")
        if (parts != null) {
            return if (parts.size > 1) {
                parts.drop(1).joinToString(" ")
            } else {
                ""
            }
        }
        return ""
    }

}

@Parcelize
data class ProfilePicture(
    @SerializedName("__type")
    val type: String?,
    val name: String?,
    val url: String?
) : Parcelable
