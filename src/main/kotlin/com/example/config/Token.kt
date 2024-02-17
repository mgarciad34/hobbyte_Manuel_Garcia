package com.example.config

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.algorithms.Algorithm.HMAC256
import com.example.models.Usuario
import io.ktor.server.config.*
import java.util.*

class Token {
    companion object {
        // Función para generar un token JWT
        fun generateJWTToken(rol:String):String{
            val token = JWT.create()
   //             .withAudience("http://127.0.0.1:8080/rutasVarias")
 //               .withIssuer("http://127.0.0.1:8080")
                .withClaim("rol", rol)
//            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.HMAC256("algo"))

            return token
        }

        fun verifyJWTToken() : JWTVerifier {
            return JWT.require(HMAC256("algo"))
             //   .withAudience(audience)
             //   .withIssuer(issuer)
                .build()
        }
    }
}