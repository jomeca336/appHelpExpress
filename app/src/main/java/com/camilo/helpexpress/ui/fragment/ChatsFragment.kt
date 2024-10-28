package com.camilo.helpexpress.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camilo.helpexpress.R
import com.camilo.helpexpress.ui.adapter.ChatAdapter
import com.camilo.helpexpress.ui.chat.ChatActivity
import com.camilo.helpexpress.ui.entity.Chat
import com.camilo.helpexpress.ui.provider.MessageProvider
import com.google.firebase.auth.FirebaseAuth


class ChatsFragment : Fragment() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatList: MutableList<Chat>
    private lateinit var messageProvider: MessageProvider
    private val currentUserId: String? = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var progressBar: ProgressBar
    private lateinit var chatsLinear: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chats, container, false)

        // Inicializar el MessageProvider
        messageProvider = MessageProvider()

        // Inicializar la lista de chats
        chatList = mutableListOf()

        // Inicializar el ProgressBar y el LinearLayout
        progressBar = view.findViewById(R.id.progressBar)
        chatsLinear = view.findViewById(R.id.chatsLinear)

        // Configurar el RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.chatsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        chatAdapter = ChatAdapter(chatList) { chat ->
            // Acción cuando el usuario hace clic en un chat (Abrir pantalla de conversación)
            openChat(chat.chatId)
        }
        recyclerView.adapter = chatAdapter

        // Cargar las conversaciones (chats) del usuario
        loadChats()

        return view
    }

    private fun loadChats() {
        currentUserId?.let { userId ->
            messageProvider.listenForChatUpdates(userId) { chats ->
                chatList.clear()
                chatList.addAll(chats)

                // Ordenar los chats por `lastMessageTimestamp` del más reciente al más antiguo
                chatList.sortByDescending { it.lastMessageTimestamp?.seconds }

                chatAdapter.notifyDataSetChanged()  // Notificar al adaptador para actualizar la lista

                // Mostrar el contenido y ocultar el progressBar
                progressBar.visibility = View.GONE
                chatsLinear.visibility = View.VISIBLE
            }
        }
    }


    // Método para abrir la pantalla de chat
    private fun openChat(chatId: String) {
        // Aquí se puede iniciar una nueva actividad o navegar a otro fragmento
        val intent = Intent(requireContext(), ChatActivity::class.java)
        Log.d("ChatId", "ChatId: $chatId")
        intent.putExtra("chatId", chatId)
        startActivity(intent)
    }
}
