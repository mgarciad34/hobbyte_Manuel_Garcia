package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioPersonaje(
    var id: Int = 0,
    val idPartida: Int? = null,
    val vida: Int? = null
)