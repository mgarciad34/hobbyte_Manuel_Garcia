package modelo

class Personaje {
    var nombre: String
    var tipoPrueba: String
    var puntuacionMaxima: Int

    constructor(nombre: String, tipoPrueba:String, puntuacionMaxima:Int){
        this.nombre = nombre
        this.tipoPrueba = tipoPrueba
        this.puntuacionMaxima = puntuacionMaxima
    }
}