package com.example.plugins

import com.example.config.Token
import com.example.config.generarTablero
import com.example.controllers.ControladorPartida
import com.example.controllers.ControladorUsuario
import com.example.models.LoginRequest
import com.example.models.Partida
import com.example.models.Usuario
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

//Aqui llamamos a las clases de los controladores
val controladorUsuario = ControladorUsuario()
val controladorPartida = ControladorPartida()


//Archivo para lanzar todas las peticiones de la API
fun Application.configureRouting() {
    routing {
        get("/api") {
            call.respondText { "Servidor abierto en el puerto 8080" }
        }
        //Ruta para registrar un usuario
        post("/api/registrar/usuario") {
           try {
                val crearUsuario = call.receive<Usuario>()
                val registrado = controladorUsuario.registrarUsuario(crearUsuario)

                if (registrado) {
                    call.respond(HttpStatusCode.OK, "Usuario registrado exitosamente")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Error al registrar usuario")
                }
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.InternalServerError, "Formato de datos no válido")
            }
        }
        post("/api/login") {
            val request = call.receive<LoginRequest>()
            val usuario = controladorUsuario.loginUsuario(request.correo, request.contrasena)
            if(usuario != "0"){
                val token = Token.generateJWTToken(usuario)
                call.respond(HttpStatusCode.OK, token)
            }

        }
        post("/api/crear/tablero") {
            try {
                val partida = call.receive<Partida>()

                if (partida.idUsuario != null) {
                    val generarTablero = generarTablero()

                    val casillas = call.parameters["casillas"]?.toIntOrNull() ?: 20
                    val tablero = generarTablero.crearTablero(casillas)

                    partida.tablero = tablero.toString()

                    if (controladorPartida.crearPartida(partida)) {
                        call.respondText("Tablero creado con éxito. ID de partida: ${partida.id}")
                    } else {
                        call.respondText("Error al crear el tablero.")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Error: No se proporcionó el ID de usuario o es inválido.")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error interno del servidor: ${e.message}")
            }
        }
    }
}
