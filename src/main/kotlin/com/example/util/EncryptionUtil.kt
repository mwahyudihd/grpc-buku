package com.example.util

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionUtil {
    private const val ALGORITHM = "AES/CBC/PKCS5Padding"

    // Kunci AES-256 tetap (HARUS 32 byte / 256 bit)
    private val SECRET_KEY = "0123456789abcdef0123456789abcdef".toByteArray()
    private val IV = "abcdefghijklmnop".toByteArray() // IV harus 16 byte

    private val keySpec = SecretKeySpec(SECRET_KEY, "AES")
    private val ivSpec = IvParameterSpec(IV)

    fun encrypt(value: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        val encrypted = cipher.doFinal(value.toByteArray())
        return Base64.getEncoder().encodeToString(encrypted)
    }

    fun decrypt(encrypted: String): String {
        return try {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            val decodedBytes = Base64.getDecoder().decode(encrypted)
            String(cipher.doFinal(decodedBytes))
        } catch (e: Exception) {
            "ERROR_DECRYPTING" // Return error agar tidak crash
        }
    }
}
