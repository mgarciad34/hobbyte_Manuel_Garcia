package com.example.controller
import com.example.db.UsuariosDB
import com.example.model.Usuario
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*

object UsuariosController {

    private val usuariosDB = UsuariosDB()
    suspend fun agregarUsuario(call: ApplicationCall, usuario: Usuario) {
        val codigoError = usuariosDB.insertarUsuario(usuario)

        if (codigoError ==  0) {
            call.respond(HttpStatusCode.OK, "Usuario añadido.")
        } else {
            // Assuming 'codigoError' contains a meaningful error message
            call.respond(HttpStatusCode.BadRequest, "Error al añadir al usuario $codigoError")
        }
    }

}