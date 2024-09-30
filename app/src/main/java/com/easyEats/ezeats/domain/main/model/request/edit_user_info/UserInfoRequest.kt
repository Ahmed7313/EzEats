package com.easyEats.ezeats.domain.main.model.request.edit_user_info

data class UserInfoRequest(
    val name: String?,
    val email: String?,
    val mobileNumber: String?,
    val profilePicture: String?,
    val birthDate:String?
)


