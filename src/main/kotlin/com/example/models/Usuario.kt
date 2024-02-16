package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    var id: Int = 0,
    val nombre: String? = null,
    val rol: String? = null,
    val correo: String? = null,
    val contrasena: String? = null
)


