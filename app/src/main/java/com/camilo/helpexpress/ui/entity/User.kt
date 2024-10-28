package com.camilo.helpexpress.ui.entity

data class User(
    val uid: String = "",            // ID del usuario en Firebase
    val nombre: String = "",          // Nombre del usuario
    val correo: String = "",          // Correo electrónico
    val descripcion: String = "",     // Descripción del usuario
    val direccion: String = "",       // Dirección del usuario
    val edad: String = "",            // Edad del usuario
    val telefono: String = ""         // Teléfono del usuario
)
