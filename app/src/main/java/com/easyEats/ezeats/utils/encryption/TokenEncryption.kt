package com.easyEats.ezeats.utils.encryption

import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import java.security.SecureRandom
import android.util.Base64
import android.util.Log
import com.easyEats.ezeats.MyApplication.Companion.TAG
import java.nio.charset.StandardCharsets

fun deriveAESKeyFromPassword(password: String, salt: ByteArray): SecretKeySpec {
    val keySpec = PBEKeySpec(password.toCharArray(), salt, 65536, 256) // 256-bit key
    val keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    val keyBytes = keyFactory.generateSecret(keySpec).encoded

    // Log the secret key in Base64 format
    val secretKeyBase64 = Base64.encodeToString(keyBytes, Base64.DEFAULT)
    Log.d(TAG,"Derived AES Key: $secretKeyBase64")

    return SecretKeySpec(keyBytes, "AES")
}

fun encryptToken(token: String, password: String): String {
    // Generate a salt (you should store this for decryption)
    val salt = ByteArray(16)
    SecureRandom().nextBytes(salt)

    // Derive the AES key from the password
    val secretKey = deriveAESKeyFromPassword(password, salt)

    // Generate a random IV
    val iv = ByteArray(12) // 12 bytes IV for GCM
    SecureRandom().nextBytes(iv)

    // Initialize the cipher
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    val gcmSpec = GCMParameterSpec(128, iv) // 128-bit authentication tag
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec)

    // Encrypt the token
    val encryptedToken = cipher.doFinal(token.toByteArray(StandardCharsets.UTF_8))

    // Combine salt, IV, and encrypted token
    val ivAndEncrypted = salt + iv + encryptedToken

    // Encode result to URL-safe Base64
    Log.d(TAG, "encryptToken: ${Base64.encodeToString(ivAndEncrypted, Base64.URL_SAFE or Base64.NO_WRAP)}")
    return Base64.encodeToString(ivAndEncrypted, Base64.URL_SAFE or Base64.NO_WRAP)
}

