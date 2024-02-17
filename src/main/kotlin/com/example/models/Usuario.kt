package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    var id: Int = 0,
    var nombre: String? = null,
    var rol: String? = null,
    var correo: String? = null,
    var contrasena: String? = null
)


