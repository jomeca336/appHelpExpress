package com.camilo.helpexpress.ui.entity

data class Chat(
    var chatId: String = "",
    val user1Id: String = "",
    val user2Id: String = "",
    val lastMessage: String = "",
    val lastMessageTimestamp: com.google.firebase.Timestamp? = null
)
