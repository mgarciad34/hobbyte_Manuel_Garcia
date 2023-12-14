package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val estado: String? = null
)
