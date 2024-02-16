package com.example.plugins

import com.example.controllers.ControladorUsuario
import com.example.models.Usuario
import com.example.models.Personaje
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
       post("api/login") {
           /*try {
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
           }*/
       }
    }
}
