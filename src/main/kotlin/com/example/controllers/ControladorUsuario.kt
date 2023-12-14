package com.example.controllers

import com.example.config.Constantes
import com.example.config.Database
import java.sql.SQLException

class ControladorUsuario {
    /*fun registrarUsuario(usuario: Usuario): Boolean {
        try {
            Database.abrirConexion()

            val sentencia = "INSERT INTO ${Constantes.TablaUsuarios} (dni, nombre, tfno, clave) VALUES (?, ?, ?, ?)"
            val pstmt = Database.conexion!!.prepareStatement(sentencia)

            pstmt.setString(1, usuario.nombre)
            pstmt.setString(2, usuario.apellidos)
            pstmt.setString(3, usuario.correo)
            pstmt.setString(4, usuario.contraseña)

            val filasAfectadas = pstmt.executeUpdate()

            return filasAfectadas > 0
        } catch (ex: SQLException) {
            ex.printStackTrace()
        } finally {
            Database.cerrarConexion()
        }

        return false
    }*/

}