package com.example.controllers

import com.example.config.Constantes
import com.example.config.Database
import java.sql.PreparedStatement
import com.example.models.UsuarioPersonaje
import java.sql.SQLException

class ControladorUsuarioPersonaje() {

    fun crearUsuarioPersonaje(usuarioPersonaje: UsuarioPersonaje): Boolean {
        try {
            Database.abrirConexion()
            // Aquí podrías agregar validaciones o verificaciones necesarias

            val sentencia =
                "INSERT INTO ${Constantes.tabla_UsuarioPersonaje} (id_partida, vida) VALUES (?, ?)"

            val pstmt: PreparedStatement = if (usuarioPersonaje.id == 0) {
                Database.conexion!!.prepareStatement(sentencia, PreparedStatement.RETURN_GENERATED_KEYS)
            } else {
                Database.conexion!!.prepareStatement(sentencia)
            }

            pstmt.setObject(1, usuarioPersonaje.idPartida)
            pstmt.setObject(2, usuarioPersonaje.vida)

            val filasAfectadas = pstmt.executeUpdate()

            if (filasAfectadas > 0 && usuarioPersonaje.id == 0) {
                val rs = pstmt.generatedKeys
                if (rs.next()) {
                    usuarioPersonaje.id = rs.getInt(1)
                }
            }

            return filasAfectadas > 0
        } catch (ex: SQLException) {
            ex.printStackTrace()
        } finally {
            Database.cerrarConexion()
        }

        return false
    }
}
