package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioPersonaje(
    var id: Int = 0,
    val idPartida: Int? = null,
    var magia: Int? = null,
    var fuerza: Int? = null,
    var habilidad: Int? = null,
    var prueba: Int? = null

)