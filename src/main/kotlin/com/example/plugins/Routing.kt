package com.example.plugins

import com.auth0.jwt.exceptions.JWTVerificationException
import com.example.config.TokenManager
import com.example.config.generarTablero
import com.example.controllers.ControladorPartida
import com.example.controllers.ControladorUsuario
import com.example.controllers.ControladorUsuarioPersonaje
import com.example.controllers.Juego
import com.example.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// Aquí llamamos a las clases de los controladores
val controladorUsuario = ControladorUsuario()
val controladorPartida = ControladorPartida()
val controladorUsuarioPersonaje = ControladorUsuarioPersonaje()
val juego = Juego()

// Aquí llamamos al token
val tokenManager = TokenManager()

// Archivo para lanzar todas las peticiones de la API
fun Application.configureRouting() {
    routing {
        get("/api") {
            call.respondText { "Servidor abierto en el puerto 8080" }
        }
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

                    if (rolUsuario == "usuario") {
                        val idUsuario = call.parameters["idUsuario"]?.toIntOrNull()
                        if (idUsuario == null) {
                            call.respond(HttpStatusCode.BadRequest, "ID de usuario no válido")
                            return@get
                        } else {
                            if (idUsuario == idUsuarioToken) {
                                    val partidas = controladorPartida.obtenerPartidasPorUsuario(idUsuarioToken)
                                    call.respond(HttpStatusCode.OK, partidas)
                                } else {
                                    call.respond(HttpStatusCode.BadRequest, "El ID no coincide con el del token")
                                }
                            }
                        } else {
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
        get("/api/obtener/tablero/{id_usuario}/{id_tablero}") {
            try {
                val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")
                if (token != null) {
                    val verificarToken = tokenManager.verifyJWTToken()
                    val jwt = verificarToken.verify(token)
                    val idUsuarioToken = jwt.getClaim("id").asInt()
                    val rolUsuario = jwt.getClaim("rol").asString()

                    val idUsuario = call.parameters["id_usuario"]?.toIntOrNull()
                    val idUsuarioPersonaje = call.parameters["id_tablero"]?.toIntOrNull()

                    if (idUsuario == null || idUsuarioPersonaje == null || rolUsuario != "usuario") {
                        call.respond(HttpStatusCode.BadRequest, "Parámetros de ruta no válidos")
                        return@get
                    }

                    val usuarioPersonaje = controladorUsuarioPersonaje.obtenerTablero(idUsuario, idUsuarioPersonaje)

                    if (usuarioPersonaje != null) {
                        call.respond(HttpStatusCode.OK, usuarioPersonaje)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Usuario o tablero incorrecto")
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
        put("/api/actualizar/tablero/{id_usuario}/{id_tablero}") {
            try {
                val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")
                if (token != null) {
                    val verificarToken = tokenManager.verifyJWTToken()
                    val jwt = verificarToken.verify(token)
                    val idUsuarioToken = jwt.getClaim("id").asInt()
                    val rolUsuario = jwt.getClaim("rol").asString()

                    val idUsuario = call.parameters["id_usuario"]?.toIntOrNull()
                    val idUsuarioPersonaje = call.parameters["id_tablero"]?.toIntOrNull()

                    if (idUsuario == null || idUsuarioPersonaje == null || rolUsuario != "usuario") {
                        call.respond(HttpStatusCode.BadRequest, "Parámetros de ruta no válidos")
                        return@put
                    }

                    var usuarioPersonaje = controladorUsuarioPersonaje.obtenerTablero(idUsuario, idUsuarioPersonaje)

                    if (usuarioPersonaje != null) {
                        val tablero = controladorPartida.obtenerTablero(idUsuarioToken)

                        var listaTablero: List<String> = emptyList()
                        var elemento: String = ""

                        for (verTablero in tablero) {
                            listaTablero = verTablero.substring(1, verTablero.length - 1).split(", ").take(20)
                            elemento = listaTablero.getOrElse(usuarioPersonaje.prueba!!) { "" }
                        }

                        var simulacion: UsuarioPersonaje? = null
                        var nPrueba: Int = 0


                        if (usuarioPersonaje.prueba!! < listaTablero.size) {
                            val letra = elemento.substring(0, 1)
                            val numero = elemento.substring(1)

                            var personajeJugador: String = ""
                            if (letra == "M") {
                                personajeJugador = "Gandalf"
                            } else if (letra == "T") {
                                personajeJugador = "Thorin"
                            } else {
                                personajeJugador = "Bilbo"
                            }
                            simulacion = juego.jugarHobbyte(letra, numero.toInt(), usuarioPersonaje)
                            controladorUsuarioPersonaje.actualizarUsuarioPersonaje(simulacion!!)
                        }

                        usuarioPersonaje = controladorUsuarioPersonaje.obtenerTablero(idUsuario, idUsuarioPersonaje)

                        if (usuarioPersonaje != null) {
                            if(usuarioPersonaje.prueba!! < 20){
                                call.respond(HttpStatusCode.OK, usuarioPersonaje)
                            } else {
                                var vidaGandalf = usuarioPersonaje.magia!!.toInt()
                                var vidaThorin = usuarioPersonaje.fuerza!!.toInt()
                                var vidaBilbo = usuarioPersonaje.habilidad!!.toInt()

                                if(vidaGandalf > 0 || vidaThorin > 0 || vidaBilbo > 0){
                                    call.respond(HttpStatusCode.OK, "Ganaste")
                                }else{
                                    call.respond(HttpStatusCode.OK, "Perdiste")
                                }

                            }
                        }
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Usuario o tablero incorrecto")
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
        post("/administrador/registrar/usuario") {
            try {
                val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")
                if (token != null) {
                    val verificarToken = tokenManager.verifyJWTToken()
                    val jwt = verificarToken.verify(token)
                    val rol = jwt.getClaim("rol").asString()

                    if (rol == "administrador") {
                        val crearUsuario = call.receive<Usuario>()
                        val registrado = controladorUsuario.registrarUsuario(crearUsuario)

                        if (registrado) {
                            call.respond(HttpStatusCode.OK, "Usuario registrado exitosamente")
                        } else {
                            call.respond(HttpStatusCode.BadRequest, "Error al registrar usuario")
                        }
                    } else {
                        call.respond(HttpStatusCode.Unauthorized, "No tienes los roles necesarios")
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "No autorizado")
                }
            } catch (e: JWTVerificationException) {
                call.respond(HttpStatusCode.Unauthorized, "Error al verificar el token")
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.InternalServerError, "Formato de datos no válido")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error del servidor")
            }
        }
        delete("/administrador/eliminar/usuario/{id}") {
            try {
                val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")
                if (token != null) {
                    val verificarToken = tokenManager.verifyJWTToken()
                    val jwt = verificarToken.verify(token)
                    val rol = jwt.getClaim("rol").asString()

                    if (rol == "administrador") {
                        val idUsuario = call.parameters["id"]?.toIntOrNull()
                        if (idUsuario == null) {
                            call.respond(HttpStatusCode.BadRequest, "ID de usuario no válido")
                            return@delete
                        } else {
                            val eliminado = controladorUsuario.eliminarUsuario(idUsuario)
                            if (eliminado) {
                                call.respond(HttpStatusCode.OK, "Usuario eliminado exitosamente")
                            } else {
                                call.respond(HttpStatusCode.BadRequest, "El usuario ya no existe")
                            }
                        }
                    } else {
                        call.respond(HttpStatusCode.Unauthorized, "No tienes permisos")
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "No autorizado")
                }
            } catch (e: JWTVerificationException) {
                call.respond(HttpStatusCode.Unauthorized, "No has verificado el token")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error del servidor")
            }
        }
        get("/administrador/usuarios") {
            try {
                val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")
                if (token != null) {
                    val verificarToken = tokenManager.verifyJWTToken()
                    val jwt = verificarToken.verify(token)
                    val rol = jwt.getClaim("rol").asString()

                    if (rol == "administrador") {
                        val usuarios = controladorUsuario.consultarUsuarios()
                        call.respond(HttpStatusCode.OK, usuarios)
                    } else {
                        call.respond(HttpStatusCode.Unauthorized, "No tienes permisos")
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "No autorizado")
                }
            } catch (e: JWTVerificationException) {
                call.respond(HttpStatusCode.Unauthorized, "No has verificado el token")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error del servidor")
            }
        }
        get("/administrador/usuario/{id}") {
            try {
                val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")
                if (token != null) {
                    val verificarToken = tokenManager.verifyJWTToken()
                    val jwt = verificarToken.verify(token)
                    val rol = jwt.getClaim("rol").asString()

                    if (rol == "administrador") {
                        val idUsuario = call.parameters["id"]?.toIntOrNull()
                        if (idUsuario == null) {
                            call.respond(HttpStatusCode.BadRequest, "ID de usuario no válido")
                            return@get
                        }

                        val usuario = controladorUsuario.consultarUsuarioId(idUsuario)
                        if (usuario != null) {
                            call.respond(HttpStatusCode.OK, usuario)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
                        }
                    } else {
                        call.respond(HttpStatusCode.Unauthorized, "No tienes permisos")
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "No autorizado")
                }
            } catch (e: JWTVerificationException) {
                call.respond(HttpStatusCode.Unauthorized, "No has verificado el token")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error del servidor")
            }
        }

    }
}
