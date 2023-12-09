package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import utils.FactoriaJuego

//Generamos el tablero
val tablero = FactoriaJuego.generarTablero()

//Archivo para lanzar todas las peticiones de la API
fun Application.configureRouting() {
    routing {
        get("/api") {
            call.respondText { "Servidor abierto en el puerto 8080" }
        }
        get("/api/mostrar/tablero"){
            call.respond(tablero)
        }
    }
}
