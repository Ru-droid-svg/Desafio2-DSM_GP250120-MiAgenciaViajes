package com.example.comrudygagenciaviajes

data class Destino(
    var id: String = "",
    var nombre: String = "",
    var pais: String = "",
    var precio: Double = 0.0,
    var descripcion: String = "",
    var imagenPath: String = "",
    var uidUsuario: String = ""
)