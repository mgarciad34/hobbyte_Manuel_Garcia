package com.example.db

import com.example.model.Constantes
import com.example.model.Usuario
import java.sql.PreparedStatement
import java.sql.SQLException

class UsuariosDB {

    val conn = ConexionSQL(Constantes.servidor, Constantes.puerto, Constantes.bbdd, Constantes.usuario, Constantes.passwd)

    fun insertarUsuario(usuario: Usuario): Int {
        var codigoError =  0
        val sentenciaSql = """  
            INSERT INTO ${Constantes.TablaUsuarios} (id, nombre, rol, correo, contrasena)
            VALUES (?, ?, ?, ?, ?)
        """.trimIndent()
        try {
            conn.conectar()
            val preparedStatement: PreparedStatement = conn.prepareStatement(sentenciaSql)
            preparedStatement.setString(1, usuario.nombre)
            preparedStatement.setString(2, usuario.rol)
            preparedStatement.setString(3, usuario.correo)
            preparedStatement.setString(4, usuario.contrasena)
            preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            codigoError = e.errorCode
        } finally {
            conn.desconectar()
        }
        return codigoError
    }
}