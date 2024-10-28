package com.camilo.helpexpress.ui.chat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camilo.helpexpress.R
import com.camilo.helpexpress.ui.adapter.MessageAdapter
import com.camilo.helpexpress.ui.entity.Message
import com.camilo.helpexpress.ui.provider.MessageProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChatActivity : AppCompatActivity() {

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: MutableList<Message>
    private lateinit var messageProvider: MessageProvider
    private lateinit var chatId: String
    private val currentUserId: String? = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var usernameTextView: TextView
    private val firestore = FirebaseFirestore.getInstance()  // Instancia de Firestore
    private lateinit var moreButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Obtener el chatId desde el Intent
        chatId = intent.getStringExtra("chatId") ?: ""

        // Inicializar el MessageProvider y lista de mensajes
        messageProvider = MessageProvider()
        messageList = mutableListOf()

        // Configurar el RecyclerView para los mensajes
        val recyclerView = findViewById<RecyclerView>(R.id.messagesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        messageAdapter = MessageAdapter(messageList)
        recyclerView.adapter = messageAdapter

        // Cargar los mensajes del chat
        listenForMessages()

        // Configurar botón de regreso
        val backButton = findViewById<ImageView>(R.id.backButtonChat)
        backButton.setOnClickListener {
            finish()
        }

        // Configurar TextView para mostrar el nombre del usuario
        usernameTextView = findViewById(R.id.usernameChatTextView)
        moreButton = findViewById(R.id.moreButtonChat)
        moreButton.setOnClickListener {
            // Lógica para abrir el menú de opciones
        }

        // Obtener y mostrar el nombre del usuario con el que se está chateando
        loadChatUserName()

        // Configurar botón de envío
        val sendButton = findViewById<Button>(R.id.sendButton)
        val messageEditText = findViewById<EditText>(R.id.messageEditText)
        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                messageEditText.text.clear()
            }
        }
    }

    // Función para cargar el nombre del usuario con el que se está chateando
    private fun loadChatUserName() {
        // Obtener el UID del otro usuario a partir del chatId
        val chatRef = firestore.collection("chats").document(chatId)
        chatRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val user1Id = document.getString("user1Id")
                val user2Id = document.getString("user2Id")

                // Determinar cuál es el otro usuario
                val otherUserId = when {
                    currentUserId == user1Id -> user2Id
                    currentUserId == user2Id -> user1Id
                    else -> null // Si no coincide, manejar este caso
                }

                if (otherUserId != null && otherUserId.isNotEmpty()) {
                    // Obtener el nombre del otro usuario desde Firestore
                    firestore.collection("usuarios").document(otherUserId).get()
                        .addOnSuccessListener { userDocument ->
                            if (userDocument.exists()) {
                                val userName = userDocument.getString("nombre")
                                usernameTextView.text = userName ?: "Usuario desconocido"

                                // Configurar el moreButton para abrir UserDetailsActivity con el userId
                                moreButton.setOnClickListener {
                                    // Crear el intent para abrir UserDetailsActivity
                                    val intent = Intent(this, UserDetailsActivity::class.java)
                                    intent.putExtra("userId", otherUserId)  // Pasar el ID del usuario
                                    startActivity(intent)  // Iniciar la actividad
                                }
                            } else {
                                usernameTextView.text = "Usuario no encontrado"
                            }
                        }
                        .addOnFailureListener {
                            usernameTextView.text = "Error al cargar usuario"
                        }
                } else {
                    usernameTextView.text = "Usuario no válido"
                }
            } else {
                usernameTextView.text = "Chat no encontrado"
            }
        }.addOnFailureListener {
            usernameTextView.text = "Error al cargar chat"
        }
    }

    // Escuchar mensajes en tiempo real
    private fun listenForMessages() {
        if (chatId.isNotEmpty()) {
            messageProvider.listenForMessages(chatId) { message ->
                messageList.add(message)
                messageAdapter.notifyDataSetChanged()
            }
        } else {
            // Manejar el caso en el que chatId sea inválido
            Toast.makeText(this, "Error: chatId inválido", Toast.LENGTH_SHORT).show()
            finish()  // Cerrar la actividad si el chatId no es válido
        }
    }


    // Enviar un mensaje
    private fun sendMessage(messageText: String) {
        currentUserId?.let { senderId ->
            messageProvider.sendMessage(chatId, senderId, messageText)
        }
    }
}
