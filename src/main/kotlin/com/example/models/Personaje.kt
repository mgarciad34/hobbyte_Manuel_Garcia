package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Personaje(
    val id: Int = 0,
    val nombre: String,
    val capacidad: Int
)