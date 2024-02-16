package com.example.config

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.Date
import java.util.Base64

class Token {
    companion object {
        // Función para generar un token JWT
        fun generarToken(correo: String): String {
            val expirationTime = 86400000 // 24 horas en milisegundos
            val key = "tu_secreto" // Clave secreta para firmar el token

            val subjectBase64 = Base64.getEncoder().encodeToString(correo.toByteArray())

            return Jwts.builder()
                .setSubject(subjectBase64)
                .setExpiration(Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact()
        }
    }
}