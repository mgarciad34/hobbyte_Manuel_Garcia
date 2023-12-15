package com.example.controllers

import com.example.config.Constantes
import com.example.config.Database
import com.example.config.KtorCifrado
import java.sql.SQLException

class ControladorPersonaje {

    fun crearPersonaje():Boolean{
        try {
            Database.abrirConexion()

            val sentencia = "INSERT INTO ${Constantes.tabla_Usuario} (nombre, correo, contrasena, estado) VALUES (?, ?, ?, ?)"
            val estado = usuario.estado ?: "Activo"

            val pstmt = Database.conexion!!.prepareStatement(sentencia)
            pstmt.setString(1, usuario.nombre)
            pstmt.setString(2, usuario.correo)
            pstmt.setString(3, KtorCifrado.cifrarContrasena(usuario.contrasena))
            pstmt.setString(4, estado)
            val filasAfectadas = pstmt.executeUpdate()

            return filasAfectadas > 0
        } catch (ex: SQLException) {
            ex.printStackTrace()
        } finally {
            Database.cerrarConexion()
        }

        return false
    }

}