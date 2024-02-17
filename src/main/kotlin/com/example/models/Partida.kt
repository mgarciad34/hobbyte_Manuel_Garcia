package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Partida(
    var id: Int = 0,
    val idUsuario: Int? = null,
    var tablero: String? = null
)

