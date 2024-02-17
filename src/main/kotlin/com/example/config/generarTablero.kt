package com.example.config

class generarTablero {

    //Funcion para crear el tablero
    fun crearTablero(casillas: Int): List<String>{
        val categoriaPruebas = listOf("M", "F", "H")
        val puntos = listOf(5, 10, 15, 20, 25, 30, 35, 40, 45, 50)
        val pruebasListadas = mutableListOf<String>()
        for (i in 1..casillas) {
            var pruebaSeleccionada = categoriaPruebas.random()
            var puntuacionSeleccionada = puntos.random()
            pruebasListadas.add(pruebaSeleccionada+puntuacionSeleccionada)
        }
        return pruebasListadas
    }
}