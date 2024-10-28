package com.camilo.helpexpress.ui.entity

import com.google.firebase.Timestamp

data class Task(
   var id: String,
   var uid: String,
   var title: String,
   var description: String,
   var category: String,
   var location: String,
   var price: Double,
   var cash: Boolean,
   var timestamp: Timestamp
){
   // Constructor vacío requerido por Firestore
   constructor() : this("", "", "", "", "", "", 0.0, false, Timestamp.now())
}
