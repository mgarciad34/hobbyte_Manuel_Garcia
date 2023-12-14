package com.example.plugins

import com.example.controllers.ControladorUsuario
import com.example.models.Usuario
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

//Aqui llamamos a las clases de los controladores
val controladorUsuario = ControladorUsuario()


//Archivo para lanzar todas las peticiones de la API
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
                    call.respond(HttpStatusCode.InternalServerError, "Error al registrar usuario")
                }
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, "Formato de datos no válido")
            }
        }
    }
}
