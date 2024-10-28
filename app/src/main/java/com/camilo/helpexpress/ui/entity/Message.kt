package com.camilo.helpexpress.ui.entity

data class Message(
    val senderId: String = "",   // UID del remitente
    val message: String = "",
    val timestamp: com.google.firebase.Timestamp? = null
)
