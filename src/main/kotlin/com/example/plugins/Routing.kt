package com.example.plugins

import com.example.controllers.ControladorUsuario
import com.example.controllers.ControladorPersonaje
import com.example.models.Usuario
import com.example.models.Personaje
import com.example.models.loginUsuario
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.config.Token

//Aqui llamamos a las clases de los controladores
val controladorUsuario = ControladorUsuario()
val controladorPersonaje = ControladorPersonaje()


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
                    call.respond(HttpStatusCode.InternalServerError, "Error al registrar usuario")
                }
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, "Formato de datos no válido")
            }
        }
       post("api/login"){
           try {
               val datosLogin = call.receive<loginUsuario>()

               // Verifica que los campos obligatorios no sean nulos
               if (datosLogin.correo != null && datosLogin.contrasena != null) {
                   val autenticado = controladorUsuario.loginUsuario(datosLogin.correo, datosLogin.contrasena)

                   if (autenticado as Boolean) {
                       call.respond("Inicio de sesión exitoso.")
                   }else {
                       call.respond("Credenciales incorrectas")
                   }
               } else {
                   call.respond("Correo y contraseña son campos obligatorios")
               }
           } catch (ex: Exception) {
               call.respond("Error en el inicio de sesión: ${ex.message}")
           }
       }
        post("api/crear/personajes") {
            try {
                val credencialesUsuario = call.receive<loginUsuario>()

                if (credencialesUsuario.correo != null && credencialesUsuario.contrasena != null) {
                    val idUsuario = controladorUsuario.obtenerId(
                        credencialesUsuario.correo,
                        credencialesUsuario.contrasena
                    )

                    if (idUsuario != -1) {
                        val gandalf = Personaje("Gandalf", 50, 50, "Magia", idUsuario)
                        val thorin = Personaje("Thorin", 50, 50, "Fuerza", idUsuario)
                        val bilbo = Personaje("Bilbo", 50, 50, "Habilidad", idUsuario)

                        // Comprobar si los personajes ya existen
                        val existeGandalf = controladorPersonaje.buscarPersonaje(gandalf.nombre, idUsuario)
                        val existeThorin = controladorPersonaje.buscarPersonaje(thorin.nombre, idUsuario)
                        val existeBilbo = controladorPersonaje.buscarPersonaje(bilbo.nombre, idUsuario)

                        if (existeGandalf == -1 && existeThorin == -1 && existeBilbo == -1) {
                            // Los personajes no existen, crearlos
                            val crearGandalf = controladorPersonaje.crearPersonaje(gandalf)
                            val crearThorin = controladorPersonaje.crearPersonaje(thorin)
                            val crearBilbo = controladorPersonaje.crearPersonaje(bilbo)

                            if (crearGandalf && crearThorin && crearBilbo) {
                                call.respond(HttpStatusCode.Created, "Personajes creados correctamente")
                            } else {
                                call.respond(
                                    HttpStatusCode.InternalServerError,
                                    "Error al crear los personajes"
                                )
                            }
                        } else {
                            // Al menos uno de los personajes ya existe
                            call.respond(HttpStatusCode.BadRequest, "El usuario ya tiene estos personajes")
                        }
                    } else {
                        call.respond(HttpStatusCode.Unauthorized, "Credenciales incorrectas")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Correo y contraseña son campos obligatorios")
                }
            } catch (ex: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error en la solicitud: ${ex.message}")
            }
        }

    }
}
