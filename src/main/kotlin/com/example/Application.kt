 package com.example

 import com.auth0.jwt.JWT
 import com.auth0.jwt.algorithms.Algorithm
 import com.example.config.Parametros
 import com.example.config.TokenManager
 import io.ktor.server.application.*
 import io.ktor.server.engine.*
 import io.ktor.server.netty.*
 import com.example.plugins.*
 import com.typesafe.config.ConfigFactory
 import io.ktor.server.auth.*
 import io.ktor.server.auth.jwt.*
 import io.ktor.server.config.*
 fun main(args: Array<String>) {
     io.ktor.server.netty.EngineMain.main(args)
 }

 fun Application.module() {
//    val config = HoconApplicationConfig((ConfigFactory.load()))
//    val tokenManager = TokenManager(config)
     val tokenManager = TokenManager()

     configureSerialization()
     configureRouting()

     install(Authentication) {

         // jwt("auth-jwt") {
         jwt {
             verifier(tokenManager.verifyJWTToken())
             //Definimos la audiencia
             //realm = config.property("realm").getString()
             realm = Parametros.realm
             //Validamos el token
             validate {
                 if (it.payload.getClaim("dni").asString().isNotEmpty()){
                     JWTPrincipal(it.payload)
                 }
                 else {
                     null
                 }
             }
         }
     }
 }
