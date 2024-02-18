package com.example.controllers

import com.example.models.UsuarioPersonaje

class Juego {

    fun jugarHobbyte(letra:String, numero: Int, usuarioPersonaje: UsuarioPersonaje): UsuarioPersonaje{
        var juego: UsuarioPersonaje = usuarioPersonaje
        if(letra == "M"){
            val juego = juegoGandalf(numero, usuarioPersonaje)
        }else if (letra == "T"){
            val juego = juegoThorin(numero, usuarioPersonaje)
        }else{
            val juego = juegoBilbo(numero, usuarioPersonaje)
        }
        return juego
    }

    fun juegoGandalf(numero: Int, usuarioPersonaje: UsuarioPersonaje): UsuarioPersonaje {
        var porcentaje = numero * 0.9
        var resultado  = 0
        if (usuarioPersonaje.magia!! > porcentaje) {
            usuarioPersonaje.magia = usuarioPersonaje.magia!! - porcentaje.toInt()
        }else{
            porcentaje = numero * 0.7
            if (usuarioPersonaje.magia!! > porcentaje) {
                usuarioPersonaje.magia = usuarioPersonaje.magia!! - porcentaje.toInt()
            }else{
                porcentaje = numero * 0.5
                if (usuarioPersonaje.magia!! < porcentaje) {
                    usuarioPersonaje.magia = usuarioPersonaje.magia!! - porcentaje.toInt()
                }else{
                    usuarioPersonaje.magia = 0
                }
            }
        }
        return usuarioPersonaje
    }

    fun juegoThorin(numero: Int, usuarioPersonaje: UsuarioPersonaje): UsuarioPersonaje {
        var porcentaje = numero * 0.9
        if (usuarioPersonaje.fuerza!! > porcentaje) {
            usuarioPersonaje.fuerza = usuarioPersonaje.fuerza!! - porcentaje.toInt()
        }else{
            porcentaje = numero * 0.7
            if (usuarioPersonaje.fuerza!! > porcentaje) {
                usuarioPersonaje.fuerza = usuarioPersonaje.fuerza!! - porcentaje.toInt()
            }else{
                porcentaje = numero * 0.5
                if (usuarioPersonaje.fuerza!! < porcentaje) {
                    usuarioPersonaje.fuerza = usuarioPersonaje.fuerza!! - porcentaje.toInt()
                }else{
                    usuarioPersonaje.fuerza = 0
                }
            }
        }
        return usuarioPersonaje
    }

    fun juegoBilbo(numero: Int, usuarioPersonaje: UsuarioPersonaje): UsuarioPersonaje {
        var porcentaje = numero * 0.9
        if (usuarioPersonaje.habilidad!! > porcentaje) {
            usuarioPersonaje.habilidad = usuarioPersonaje.habilidad!! - porcentaje.toInt()
        } else {
            porcentaje = numero * 0.7
            if (usuarioPersonaje.habilidad!! > porcentaje) {
                usuarioPersonaje.habilidad = usuarioPersonaje.habilidad!! - porcentaje.toInt()
            } else {
                porcentaje = numero * 0.5
                if (usuarioPersonaje.habilidad!! < porcentaje) {
                    usuarioPersonaje.habilidad = usuarioPersonaje.habilidad!! - porcentaje.toInt()
                } else {
                    usuarioPersonaje.habilidad = 0
                }
            }
        }
        return usuarioPersonaje
    }



}

