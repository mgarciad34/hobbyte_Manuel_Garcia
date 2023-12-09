package utils

import kotlin.random.Random

class FactoriaJuego {
    companion object{
        //Creamos las funciones que necesitamos para la API
        fun generarTablero(): List<Triple<String, String, Int>> {
            val tablero = mutableListOf<Triple<String, String, Int>>()
            val esfuerzo = listOf(5, 10, 15, 20, 25, 30, 35, 40, 45, 50)

            for (i in 1..20) {
                val numero = Random.nextInt(1, 4)
                val esfuerzoIndex = Random.nextInt(0, esfuerzo.size)
                val valorEsfuerzo = esfuerzo[esfuerzoIndex]

                if (numero == 1) {
                    tablero.add(Triple("Prueba $i", "Magia", valorEsfuerzo))
                } else if (numero == 2) {
                    tablero.add(Triple("Prueba $i", "Fuerza", valorEsfuerzo))
                } else {
                    tablero.add(Triple("Prueba $i", "Habilidad", valorEsfuerzo))
                }
            }
            return tablero
        }
    }
}