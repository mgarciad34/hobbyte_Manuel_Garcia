package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(val nombre: String, val rol: String,  val correo: String, val contrasena: String)
