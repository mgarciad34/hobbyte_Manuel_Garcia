package com.example.config

import java.security.MessageDigest
import javax.crypto.spec.SecretKeySpec
import java.util.Base64
import javax.crypto.Cipher

class KtorCifrado {
    companion object {
        fun cifrarContrasena(contrasena: String): String {
            val MD5 = MessageDigest.getInstance("MD5")
            val contrasenaBytes = contrasena.toByteArray(Charsets.UTF_8)
            val contrasenaCifradaBytes = MD5.digest(contrasenaBytes)

            return contrasenaCifradaBytes.joinToString("") {
                "%02x".format(it)
            }
        }

        fun descifrarContrasena(contrasenaCifrada: String): String {
            val cifrador = Cipher.getInstance("MD5")
            val clave = SecretKeySpec(contrasenaCifrada.toByteArray(Charsets.UTF_8), "MD5")
            cifrador.init(Cipher.DECRYPT_MODE, clave)

            val contrasenaBytes = cifrador.doFinal(Base64.getDecoder().decode(contrasenaCifrada))
            return String(contrasenaBytes, Charsets.UTF_8)
        }

    }
}