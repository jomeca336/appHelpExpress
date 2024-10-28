package com.camilo.helpexpress.ui.entity

data class Notification(
    var id: String,
    var uid: String,
    var title: String,
    var description: String,
    var category: String
){
    // Constructor sin argumentos (necesario para Firestore)
    constructor() : this("", "", "", "", "")}
