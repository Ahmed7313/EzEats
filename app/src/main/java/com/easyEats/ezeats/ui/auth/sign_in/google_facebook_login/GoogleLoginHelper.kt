package com.easyEats.ezeats.ui.auth.sign_in.google_facebook_login

import android.content.Intent
import androidx.fragment.app.Fragment
import com.easyEats.ezeats.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


class GoogleLoginHelper(
    private val fragment: Fragment,
    private val callback: SocialLoginCallback
) {

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(fragment.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        GoogleSignIn.getClient(fragment.requireContext(), gso)
    }

    fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        fragment.startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    fun handleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            val email = account?.email
            if (!email.isNullOrEmpty()) {
                callback.onLoginSuccess(email)
            } else {
                callback.onLoginError("Email is not available.")
            }
        } catch (e: ApiException) {
            callback.onLoginError("Google Sign-In failed: ${e.message}")
        }
    }

    companion object {
        const val GOOGLE_SIGN_IN_REQUEST_CODE = 1001
    }
}

