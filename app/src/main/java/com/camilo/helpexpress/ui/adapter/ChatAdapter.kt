package com.camilo.helpexpress.ui.adapter

import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.camilo.helpexpress.R
import com.camilo.helpexpress.ui.entity.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChatAdapter(private val chatList: MutableList<Chat>, private val onChatClicked: (Chat) -> Unit) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private val db = FirebaseFirestore.getInstance()  // Instancia de Firestore

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        val lastMessageTextView: TextView = itemView.findViewById(R.id.lastMessageTextView)
        val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val currentChat = chatList[position]

        // Obtener el UID del otro usuario
        val otherUserId = if (currentChat.user1Id == FirebaseAuth.getInstance().currentUser?.uid) {
            currentChat.user2Id
        } else {
            currentChat.user1Id
        }

        // Obtener el nombre del otro usuario desde Firestore
        getUserName(otherUserId) { userName ->
            holder.usernameTextView.text = userName ?: "Usuario desconocido"
        }

        // Mostrar el último mensaje
        holder.lastMessageTextView.text = currentChat.lastMessage

        // Mostrar el tiempo relativo (por ejemplo, "hace 2 minutos")
        val now = System.currentTimeMillis()
        val relativeTime = DateUtils.getRelativeTimeSpanString(
            (currentChat.lastMessageTimestamp?.seconds ?: now / 1000) * 1000,
            now,
            DateUtils.MINUTE_IN_MILLIS
        )
        holder.timestampTextView.text = relativeTime

        // Al hacer clic en un chat, abrir la conversación
        holder.itemView.setOnClickListener {
            try {
                onChatClicked(currentChat)
            } catch (e: Exception) {
                Log.e("ChatAdapter", "Error al hacer clic en el chat: ${e.message}")
            }
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    fun sortChatsByDate() {
        chatList.sortByDescending { it.lastMessageTimestamp?.seconds }  // Ordenar por el timestamp más reciente
        notifyDataSetChanged()  // Notificar al adaptador para actualizar la vista
    }

    // Función para obtener el nombre de usuario desde Firestore
    private fun getUserName(userId: String, onResult: (String?) -> Unit) {
        if (userId.isNotEmpty()) {
            db.collection("usuarios").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val userName = document.getString("nombre")
                        onResult(userName)  // Devolver el nombre del usuario
                    } else {
                        onResult(null)  // Usuario no encontrado
                    }
                }
                .addOnFailureListener {
                    onResult(null)  // En caso de error, devolver null
                }
        } else {
            onResult(null)  // Manejar el caso donde el userId sea inválido
        }
    }

    // Método para actualizar la lista de chats y ordenarlos
    fun updateChats(newChats: List<Chat>) {
        chatList.clear()
        chatList.addAll(newChats)
        sortChatsByDate()  // Ordenar chats cada vez que se actualizan
    }
}
