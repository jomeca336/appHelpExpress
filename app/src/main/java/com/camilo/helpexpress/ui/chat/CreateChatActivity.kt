package com.camilo.helpexpress.ui.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camilo.helpexpress.R
import com.camilo.helpexpress.ui.adapter.UserAdapter
import com.camilo.helpexpress.ui.entity.User
import com.camilo.helpexpress.ui.provider.MessageProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateChatActivity : AppCompatActivity() {

    private lateinit var userAdapter: UserAdapter
    private lateinit var userList: MutableList<User>
    private lateinit var firestore: FirebaseFirestore
    private lateinit var messageProvider: MessageProvider
    private val currentUserId: String? = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_chat)

        // Inicializar Firestore y el MessageProvider
        firestore = FirebaseFirestore.getInstance()
        messageProvider = MessageProvider()

        // Inicializar la lista de usuarios
        userList = mutableListOf()

        // Configurar RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.usersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(userList) { selectedUser ->
            // Verificar si ya existe un chat con el usuario seleccionado
            checkExistingChat(selectedUser)
        }
        recyclerView.adapter = userAdapter

        // Cargar la lista de usuarios
        loadUsers()
    }

    private fun loadUsers() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        Log.d("CreateChatActivity", "Usuario actual (UID): $currentUserId")

        if (currentUserId != null) {
            firestore.collection("usuarios")
                .get()
                .addOnSuccessListener { documents ->
                    userList.clear()  // Limpiar lista antes de añadir los usuarios
                    Log.d("CreateChatActivity", "Total de documentos obtenidos: ${documents.size()}")

                    for (document in documents) {
                        val user = document.toObject(User::class.java)

                        // Log para ver qué usuarios se están obteniendo
                        Log.d("CreateChatActivity", "Usuario obtenido: ${user.uid}")

                        // Excluir el usuario actual
                        if (user.uid != null && user.uid != currentUserId) {
                            Log.d("CreateChatActivity", "Añadiendo usuario: ${user.uid}, excluyendo: $currentUserId")
                            userList.add(user)
                        } else {
                            Log.d("CreateChatActivity", "Usuario excluido (es el usuario actual): ${user.uid}")
                        }
                    }

                    if (userList.isEmpty()) {
                        Toast.makeText(this, "No hay otros usuarios disponibles", Toast.LENGTH_SHORT).show()
                        Log.d("CreateChatActivity", "No se encontraron usuarios para mostrar")
                    } else {
                        Log.d("CreateChatActivity", "Usuarios finales para mostrar: ${userList.size}")
                    }

                    userAdapter.notifyDataSetChanged()  // Refrescar la lista
                }
                .addOnFailureListener { e ->
                    Log.e("CreateChatActivity", "Error al cargar usuarios: ${e.message}")
                }
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            Log.e("CreateChatActivity", "Usuario actual no autenticado, no se puede cargar usuarios")
        }
    }


    // Verificar si ya existe un chat con el usuario seleccionado
    private fun checkExistingChat(selectedUser: User) {
        currentUserId?.let { currentUid ->
            // Verificar si ya existe un chat entre el usuario actual y el usuario seleccionado
            messageProvider.checkExistingChat(currentUid, selectedUser.uid) { existingChatId ->
                if (existingChatId != null) {
                    // Si ya existe un chat, redirigir a ese chat
                    openChat(existingChatId)
                } else {
                    // Si no existe un chat, crear uno nuevo
                    createNewChat(selectedUser)
                }
            }
        }
    }

    // Método para crear un nuevo chat
    private fun createNewChat(selectedUser: User) {
        currentUserId?.let { currentUid ->
            messageProvider.createChatIfNotExists(currentUid, selectedUser.uid) { newChatId ->
                openChat(newChatId)  // Redirigir al nuevo chat
            }
        }
    }

    // Método para abrir un chat existente
    private fun openChat(chatId: String) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatId", chatId)
        startActivity(intent)
    }

}
