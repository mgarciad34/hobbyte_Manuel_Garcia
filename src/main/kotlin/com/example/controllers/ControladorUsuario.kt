package com.example.controllers

import com.example.config.Constantes
import com.example.config.Database
import com.example.models.Usuario
import com.example.config.KtorCifrado
import java.sql.PreparedStatement
import java.sql.SQLException

class ControladorUsuario {


    fun registrarUsuario(usuario: Usuario): Boolean {
        try {
            Database.abrirConexion()
            val correoExistente = existeUsuario(usuario.correo.toString())
            if (correoExistente) {
                println("El correo electrónico ya está registrado.")
                return false
            }

            val sentencia =
                "INSERT INTO ${Constantes.tabla_Usuario} (nombre, rol, correo, contrasena) VALUES (?, ?, ?, ?)"

            val pstmt: PreparedStatement = if (usuario.id == 0) {
                Database.conexion!!.prepareStatement(sentencia, PreparedStatement.RETURN_GENERATED_KEYS)
            } else {
                Database.conexion!!.prepareStatement(sentencia)
            }

            pstmt.setString(1, usuario.nombre)
            pstmt.setString(2, usuario.rol)
            pstmt.setString(3, usuario.correo)
            pstmt.setString(4, usuario.contrasena?.let { KtorCifrado.cifrarContrasena(it) })

            val filasAfectadas = pstmt.executeUpdate()

            if (filasAfectadas > 0 && usuario.id == 0) {
                val rs = pstmt.generatedKeys
                if (rs.next()) {
                    usuario.id = rs.getInt(1)
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

    fun existeUsuario(correo: String): Boolean {
        val consulta = "SELECT COUNT(*) AS total FROM ${Constantes.tabla_Usuario} WHERE correo = ?"

        val pstmt = Database.conexion!!.prepareStatement(consulta)
        pstmt.setString(1, correo)
        val rs = pstmt.executeQuery()

        if (rs.next()) {
            val total = rs.getInt("total")
            return total > 0
        }

        return false
    }

    fun existeUsuarioId(idUsuario: String): Boolean {
        try {
            val consulta = "SELECT COUNT(*) AS total FROM ${Constantes.tabla_Usuario} WHERE id = ?"

            val pstmt = Database.conexion!!.prepareStatement(consulta)
            pstmt.setInt(1, idUsuario.toInt())
            val rs = pstmt.executeQuery()

            if (rs.next()) {
                val total = rs.getInt("total")
                return total > 0
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }

        return false
    }

    //Funcion para loguearnos
    fun loginUsuario(correo: String, contrasena: String): Usuario {
        val usuario = Usuario()
        try {
            Database.abrirConexion()

            val sentencia = "SELECT * FROM ${Constantes.tabla_Usuario} WHERE correo = ? AND contrasena = ?"
            val pstmt = Database.conexion!!.prepareStatement(sentencia)
            pstmt.setString(1, correo)
            pstmt.setString(2, KtorCifrado.cifrarContrasena(contrasena))

            val rs = pstmt.executeQuery()
            if (rs.next()) {
                usuario.id = rs.getInt("id")
                usuario.nombre = rs.getString("nombre")
                usuario.rol = rs.getString("rol")
                usuario.correo = rs.getString("correo")
                usuario.contrasena = rs.getString("contrasena")
            } else {
                usuario.id = 0 // Indica que no se encontró ningún usuario con ese correo y contraseña
            }
        } catch (ex: SQLException) {
            ex.printStackTrace() // Manejo de la excepción, podrías querer cambiar esto según tus necesidades
        } finally {
            Database.cerrarConexion()
        }
        return usuario
    }

    fun eliminarUsuario(idUsuario: Int): Boolean {
        try {
            Database.abrirConexion()

            val usuarioExiste = existeUsuarioId(idUsuario.toString())
            println(usuarioExiste)
            if (!usuarioExiste) {
                println("El usuario con ID $idUsuario no existe.")
                return false
            }

            val sentencia = "DELETE FROM ${Constantes.tabla_Usuario} WHERE id = ?"
            val pstmt = Database.conexion!!.prepareStatement(sentencia)
            pstmt.setInt(1, idUsuario)

            val filasAfectadas = pstmt.executeUpdate()

            if (filasAfectadas > 0) {
                println("Usuario con ID $idUsuario eliminado exitosamente.")
                return true
            } else {
                println("No se eliminó ningún usuario con ID $idUsuario.")
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        } finally {
            Database.cerrarConexion()
        }

        return false
    }

}