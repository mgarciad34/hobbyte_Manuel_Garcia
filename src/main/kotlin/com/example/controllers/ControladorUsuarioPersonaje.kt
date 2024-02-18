package com.example.controllers

import com.example.config.Constantes
import com.example.config.Database
import com.example.models.Usuario
import java.sql.PreparedStatement
import com.example.models.UsuarioPersonaje
import java.sql.SQLException

class ControladorUsuarioPersonaje() {

    fun crearUsuarioPersonaje(usuarioPersonaje: UsuarioPersonaje): Boolean {
        try {
            Database.abrirConexion()
            // Aquí podrías agregar validaciones o verificaciones necesarias

            val sentencia =
                "INSERT INTO ${Constantes.tabla_UsuarioPersonaje} (id_partida, magia, fuerza, habilidad, prueba) VALUES (?, ?, ?, ?, ?)"

            val pstmt: PreparedStatement = if (usuarioPersonaje.id == 0) {
                Database.conexion!!.prepareStatement(sentencia, PreparedStatement.RETURN_GENERATED_KEYS)
            } else {
                Database.conexion!!.prepareStatement(sentencia)
            }

            // Corregir los números de los índices en setObject
            pstmt.setObject(1, usuarioPersonaje.idPartida)
            pstmt.setObject(2, usuarioPersonaje.magia)
            pstmt.setObject(3, usuarioPersonaje.fuerza)
            pstmt.setObject(4, usuarioPersonaje.habilidad)
            pstmt.setObject(5, usuarioPersonaje.prueba)

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

    fun obtenerTablero(idUsuario: Int, idUsuarioPersonaje: Int): UsuarioPersonaje? {
        var usuarioPersonaje: UsuarioPersonaje? = null
        try {
            Database.abrirConexion()

            val sentencia = """
            SELECT up.*
            FROM usuariopersonaje up
            INNER JOIN partidas p ON up.id_partida = p.id
            WHERE p.id_usuario = ?
            AND up.id = ?
        """.trimIndent()
            val pstmt = Database.conexion!!.prepareStatement(sentencia)
            pstmt.setInt(1, idUsuario)
            pstmt.setInt(2, idUsuarioPersonaje)

            val rs = pstmt.executeQuery()

            if (rs.next()) {
                usuarioPersonaje = UsuarioPersonaje(
                    id = rs.getInt("id"),
                    idPartida = rs.getInt("id_partida"),
                    magia = rs.getInt("magia"),
                    fuerza = rs.getInt("fuerza"),
                    habilidad = rs.getInt("habilidad"),
                    prueba = rs.getInt("prueba")
                )
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        } finally {
            Database.cerrarConexion()
        }
        return usuarioPersonaje
    }

    fun actualizarUsuarioPersonaje(usuarioPersonaje: UsuarioPersonaje) {
        try {
            Database.abrirConexion()

            val sentencia = """
            UPDATE usuariopersonaje
            SET magia = ?,
                fuerza = ?,
                habilidad = ?,
                prueba = ?
            WHERE id = ?
        """.trimIndent()

            val pstmt = Database.conexion!!.prepareStatement(sentencia)
            pstmt.setInt(1, usuarioPersonaje.magia ?: 0)
            pstmt.setInt(2, usuarioPersonaje.fuerza ?: 0)
            pstmt.setInt(3, usuarioPersonaje.habilidad ?: 0)
            pstmt.setInt(4, usuarioPersonaje.prueba ?: 0)
            pstmt.setInt(5, usuarioPersonaje.id)

            pstmt.executeUpdate()

        } catch (ex: SQLException) {
            ex.printStackTrace()
        } finally {
            Database.cerrarConexion()
        }
    }


}
