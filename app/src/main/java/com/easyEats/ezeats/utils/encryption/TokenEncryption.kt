package com.easyEats.ezeats.utils.encryption


import android.util.Base64
import com.easyEats.ezeats.BuildConfig
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

fun encryptToken(token: String): String {
    val keySpec: SecretKey = SecretKeySpec(BuildConfig.SECRET_KEY.toByteArray(Charsets.UTF_8), "AES")
    val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, keySpec)
    val encryptedToken = cipher.doFinal(token.toByteArray(Charsets.UTF_8))
    return Base64.encodeToString(encryptedToken, Base64.URL_SAFE or Base64.NO_WRAP)
}


