package com.easyEats.ezeats.domain.main.model.request.edit_user_info

data class UserInfoRequest(
    val name: String?  = null,
    val email: String? = null,
    val mobileNumber: String? = null,
    val profilePicture: String? = null,
    val birthDate:String? = null
)


