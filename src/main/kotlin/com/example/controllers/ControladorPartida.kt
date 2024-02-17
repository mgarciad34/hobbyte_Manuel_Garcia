package com.example.controllers

import com.example.config.Constantes
import com.example.config.Database
import com.example.config.KtorCifrado
import com.example.models.Partida
import java.sql.PreparedStatement
import java.sql.SQLException

class ControladorPartida {
    fun crearPartida(partida: Partida): Boolean {
        try {
            Database.abrirConexion()
            // Aquí deberías verificar si la partida ya existe o realizar alguna validación necesaria

            val sentencia =
                "INSERT INTO ${Constantes.tabla_Partidas} (id_usuario, tablero) VALUES (?, ?)"

            val pstmt: PreparedStatement = if (partida.id == 0) {
                Database.conexion!!.prepareStatement(sentencia, PreparedStatement.RETURN_GENERATED_KEYS)
            } else {
                Database.conexion!!.prepareStatement(sentencia)
            }

            pstmt.setObject(1, partida.idUsuario)
            pstmt.setString(2, partida.tablero)

            val filasAfectadas = pstmt.executeUpdate()

            if (filasAfectadas > 0 && partida.id == 0) {
                val rs = pstmt.generatedKeys
                if (rs.next()) {
                    partida.id = rs.getInt(1)
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