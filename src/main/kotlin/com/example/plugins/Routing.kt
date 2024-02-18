package com.example.plugins

import com.auth0.jwt.exceptions.JWTVerificationException
import com.example.config.TokenManager
import com.example.config.generarTablero
import com.example.controllers.ControladorPartida
import com.example.controllers.ControladorUsuario
import com.example.controllers.ControladorUsuarioPersonaje
import com.example.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

//Aqui llamamos a las clases de los controladores
val controladorUsuario = ControladorUsuario()
val controladorPartida = ControladorPartida()
val controladorUsuarioPersonaje = ControladorUsuarioPersonaje()


//Aqui llamamos al token
val tokenManager = TokenManager()

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

            if (usuario == null) {
                call.response.status(HttpStatusCode.NotFound)
                call.respond(Respuesta("Usuario ${request.correo} incorrecto", HttpStatusCode.NotFound.value))
            } else {
                val token = tokenManager.generateJWTToken(usuario)
                call.respond(token)
            }
        }
        post("/api/crear/tablero") {
            try {
                val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")
                if (token != null) {
                    val verificarToken = tokenManager.verifyJWTToken()
                    val jwt = verificarToken.verify(token)
                    val idUsuario = jwt.getClaim("id").asInt()
                    val rolUsuario = jwt.getClaim("rol").asString()

                    if (rolUsuario == "usuario") {
                        val generarTablero = generarTablero()

                        val tablero = generarTablero.crearTablero(20)
                        val partida = Partida(
                            0, idUsuario, tablero.toString()
                        )

                        if (controladorPartida.crearPartida(partida)) {
                            call.respondText("Tablero creado con éxito. ID de partida: ${partida.id}")
                            val usuarioPersonaje = UsuarioPersonaje(0, partida.id, 50, 50, 50, 0)
                            controladorUsuarioPersonaje.crearUsuarioPersonaje(usuarioPersonaje)
                        } else {
                            call.respondText("Error al crear el tablero.")
                        }
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Error: El ID de usuario o el rol proporcionado no coincide con los del token")
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Token JWT no proporcionado en la solicitud")
                }
            } catch (e: JWTVerificationException) {
                call.respond(HttpStatusCode.Unauthorized, "Error al verificar el token JWT: ${e.message}")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error interno del servidor: ${e.message}")
            }
        }
        get("/api/obtener/partidas/{idUsuario}") {
            try {
                val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")
                if (token != null) {
                    val verificarToken = tokenManager.verifyJWTToken()
                    val jwt = verificarToken.verify(token)
                    val idUsuarioToken = jwt.getClaim("id").asInt()
                    val rolUsuario = jwt.getClaim("rol").asString()

                    if(rolUsuario == "usuario"){
                        val idUsuario = call.parameters["idUsuario"]?.toIntOrNull()
                        if (idUsuario == null) {
                            call.respond(HttpStatusCode.BadRequest, "ID de usuario no válido")
                            return@get
                        }else {
                            if(idUsuario == idUsuarioToken) {
                                val partidas = controladorPartida.obtenerPartidasPorUsuario(idUsuarioToken)
                                call.respond(HttpStatusCode.OK, partidas)
                            }else{
                                call.respond(HttpStatusCode.BadRequest, "El ID no coincide con el del token")
                            }
                        }
                    }else{
                        call.respond(HttpStatusCode.Unauthorized, "No tienes los roles necesarios")
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "No autorizado")
                }
            } catch (e: JWTVerificationException) {
                call.respond(HttpStatusCode.Unauthorized, "Error al verificar el token")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error del servidor")
            }
        }

    }
}
