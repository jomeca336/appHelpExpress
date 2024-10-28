package com.camilo.helpexpress.ui.provider



import android.util.Log
import com.camilo.helpexpress.ui.entity.Chat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import com.camilo.helpexpress.ui.entity.Message
import com.google.firebase.firestore.Query
import com.google.firebase.auth.FirebaseAuth

class MessageProvider {

    private val db = FirebaseFirestore.getInstance()

    // Crear un nuevo chat entre dos usuarios si no existe
    fun createChatIfNotExists(user1Id: String, user2Id: String, onChatCreated: (chatId: String) -> Unit) {
        val chatId = generateChatId(user1Id, user2Id)

        val chatRef = db.collection("chats").document(chatId)
        chatRef.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                val chatData = hashMapOf(
                    "user1Id" to user1Id,
                    "user2Id" to user2Id,
                    "userIds" to listOf(user1Id, user2Id)  // Aquí agregamos el array de userIds
                )
                chatRef.set(chatData).addOnSuccessListener {
                    onChatCreated(chatId)
                }
            } else {
                onChatCreated(chatId)
            }
        }
    }





    // Escuchar mensajes en tiempo real de un chat específico
    fun listenForMessages(chatId: String, onMessageReceived: (Message) -> Unit) {
        db.collection("chats").document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    e.printStackTrace()
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    for (document in snapshot.documentChanges) {
                        if (document.type == com.google.firebase.firestore.DocumentChange.Type.ADDED) {
                            val message = document.document.toObject(Message::class.java)
                            onMessageReceived(message)
                        }
                    }
                }
            }
    }

    fun generateChatId(user1Id: String?, user2Id: String?): String {
        if (user1Id == null || user2Id == null) {
            throw IllegalArgumentException("user1Id and user2Id cannot be null")
        }
        return if (user1Id < user2Id) "$user1Id$user2Id" else "$user2Id$user1Id"
    }

    fun getUserChats(userId: String, onChatsReceived: (List<Chat>) -> Unit) {
        db.collection("chats")
            .whereEqualTo("user1Id", userId)   // Buscar chats donde el usuario sea user1Id
            .get()
            .addOnSuccessListener { user1Chats ->
                db.collection("chats")
                    .whereEqualTo("user2Id", userId)  // También buscar chats donde el usuario sea user2Id
                    .get()
                    .addOnSuccessListener { user2Chats ->
                        val allChats = mutableListOf<Chat>()

                        // Agregar los chats donde es user1Id
                        for (document in user1Chats.documents) {
                            val chat = document.toObject(Chat::class.java)
                            if (chat != null) {
                                allChats.add(chat)
                            }
                        }

                        // Agregar los chats donde es user2Id
                        for (document in user2Chats.documents) {
                            val chat = document.toObject(Chat::class.java)
                            if (chat != null) {
                                allChats.add(chat)
                            }
                        }

                        // Retornar todos los chats
                        onChatsReceived(allChats)
                    }
            }
    }

    fun sendMessage(chatId: String, senderId: String, messageText: String) {
        val message = hashMapOf(
            "senderId" to senderId,
            "message" to messageText,
            "timestamp" to com.google.firebase.Timestamp.now()
        )

        db.collection("chats").document(chatId)
            .collection("messages")
            .add(message)
            .addOnSuccessListener {
                // Actualizar el documento del chat con el último mensaje y timestamp
                db.collection("chats").document(chatId)
                    .update(
                        "lastMessage", messageText,
                        "lastMessageTimestamp", com.google.firebase.Timestamp.now()
                    )
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
    fun listenForChatUpdates(userId: String, onChatsUpdated: (List<Chat>) -> Unit) {
        db.collection("chats")
            .whereArrayContains("userIds", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    e.printStackTrace()
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val updatedChats = mutableListOf<Chat>()
                    for (document in snapshot.documents) {
                        val chat = document.toObject(Chat::class.java)
                        if (chat != null) {
                            chat.chatId = document.id  // Asignar el ID del documento como el chatId
                            updatedChats.add(chat)
                        }
                    }
                    onChatsUpdated(updatedChats)
                }
            }
    }

    fun checkExistingChat(user1Id: String, user2Id: String, onChatFound: (String?) -> Unit) {
        db.collection("chats")
            .whereArrayContains("userIds", user1Id) // Buscar donde `userIds` contiene `user1Id`
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val userIds = document.get("userIds") as? List<*>
                    if (userIds?.contains(user2Id) == true) {
                        // Si encontramos un chat con ambos IDs, retornamos el ID del chat
                        onChatFound(document.id)
                        return@addOnSuccessListener
                    }
                }
                // Si no se encontró, devolvemos null
                onChatFound(null)
            }
            .addOnFailureListener {
                onChatFound(null)  // En caso de error
            }
    }






}
