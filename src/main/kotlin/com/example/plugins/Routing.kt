package com.example.plugins

import com.example.controller.UsuariosController
import com.example.model.Usuario
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

//Archivo para lanzar todas las peticiones de la API
fun Application.configureRouting() {
    routing {
        get("/api") {
            call.respondText { "Servidor abierto en el puerto 8080" }
        }
        post("/registro") {
            val usuario = call.receive<Usuario>()
            UsuariosController.agregarUsuario(call, usuario)
        }


    }
}
