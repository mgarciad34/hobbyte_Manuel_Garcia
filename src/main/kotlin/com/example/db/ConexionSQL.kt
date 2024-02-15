package com.example.db
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException
class ConexionSQL (private val host: String,
                   private val port: Int,
                   private val databaseName: String,
                   private val username: String,
                   private val password: String) {

    private var connection: Connection? = null

    fun conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            val url = "jdbc:mysql://$host:$port/$databaseName"
            connection = DriverManager.getConnection(url, username, password)
            println("Conexión exitosa a la base de datos MySQL")
        } catch (e: SQLException) {
            println("Error al conectarse a la base de datos MySQL: ${e.message}")
        } catch (e: ClassNotFoundException) {
            println("Error: No se pudo encontrar la clase del controlador JDBC")
        }
    }

    fun desconectar() {
        try {
            connection?.close()
            println("Conexión cerrada")
        } catch (e: SQLException) {
            println("Error al cerrar la conexión: ${e.message}")
        }
    }

    fun prepareStatement(query: String, vararg parameters: Any): PreparedStatement {
        val preparedStatement = connection?.prepareStatement(query)
        parameters.forEachIndexed { index, parameter ->
            preparedStatement?.setObject(index + 1, parameter)
        }
        return preparedStatement ?: throw SQLException("Error al preparar la consulta")
    }
}