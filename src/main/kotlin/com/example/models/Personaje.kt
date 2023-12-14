package com.example.models

data class Personaje(
    val id: Int,
    val nombre: String,
    val poderActual: Int,
    val poderMaximo: Int,
    val habilidad: String,
    val idUsuario: Int?
)
