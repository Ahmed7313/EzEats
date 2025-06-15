package com.easyEats.ezeats.ui.auth.sign_in.google_facebook_login

interface SocialLoginCallback {
    fun onLoginSuccess(phoneOrEmail: String)
    fun onLoginError(errorMessage: String)
}
