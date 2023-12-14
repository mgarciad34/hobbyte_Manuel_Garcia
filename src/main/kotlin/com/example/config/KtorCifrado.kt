package com.example.config

import java.security.MessageDigest
import javax.crypto.spec.SecretKeySpec
import java.util.Base64
import javax.crypto.Cipher

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

        fun descifrarContrasena(contrasenaCifrada: String): String {
            val cifrador = Cipher.getInstance("SHA-256")
            val clave = SecretKeySpec(contrasenaCifrada.toByteArray(Charsets.UTF_8), "SHA-256")
            cifrador.init(Cipher.DECRYPT_MODE, clave)

            val contrasenaBytes = cifrador.doFinal(Base64.getDecoder().decode(contrasenaCifrada))
            return String(contrasenaBytes, Charsets.UTF_8)
        }

    }
}