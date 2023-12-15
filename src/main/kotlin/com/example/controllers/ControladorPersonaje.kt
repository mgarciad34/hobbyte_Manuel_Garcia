package com.example.controllers

import com.example.config.Constantes
import com.example.models.Personaje
import com.example.config.Database
import java.sql.PreparedStatement
import java.sql.SQLException

class ControladorPersonaje {

    fun crearPersonaje(personaje: Personaje): Boolean {
        try {
            Database.abrirConexion()
            val sentencia = "INSERT IGNORE INTO ${Constantes.tabla_Personaje} (nombre, poder_actual, poder_maximo, habilidad, id_usuario) VALUES (?, ?, ?, ?, ?)"

            val pstmt: PreparedStatement = Database.conexion!!.prepareStatement(sentencia)
            pstmt.setString(1, personaje.nombre)
            pstmt.setInt(2, personaje.poderActual)
            pstmt.setInt(3, personaje.poderMaximo)
            pstmt.setString(4, personaje.habilidad)
            pstmt.setInt(5, personaje.idUsuario ?: 0)

            val filasAfectadas = pstmt.executeUpdate()

            return filasAfectadas > 0
        } catch (ex: SQLException) {
            ex.printStackTrace()
        } finally {
            Database.cerrarConexion()
        }

        return false
    }

    // Consulta Personajes
    fun buscarPersonaje(nombre: String, idUsuario: Int): Int {
        try {
            Database.abrirConexion()

            val sentencia = "SELECT * FROM ${Constantes.tabla_Personaje} WHERE nombre = ? AND id_usuario = ?"

            val pstmt = Database.conexion!!.prepareStatement(sentencia)
            pstmt.setString(1, nombre)
            pstmt.setInt(2, idUsuario)

            val resultado = pstmt.executeQuery()

            return if (resultado.next()) {
                0
            } else {
                -1
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
            return -1
        } finally {
            Database.cerrarConexion()
        }
    }


}