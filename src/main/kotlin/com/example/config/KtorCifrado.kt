package com.example.config

import java.security.MessageDigest
class KtorCifrado {
    companion object {
        fun cifrarContrasena(contrasena: String): String {
            val sha256 = MessageDigest.getInstance("SHA-256")
            val contrasenaBytes = contrasena.toByteArray(Charsets.UTF_8)
            val contrasenaCifradaBytes = sha256.digest(contrasenaBytes)

            return contrasenaCifradaBytes.joinToString("") {
                "%02x".format(it)
            }
        }
    }
}