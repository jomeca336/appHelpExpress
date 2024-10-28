package com.camilo.helpexpress.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.camilo.helpexpress.R
import com.camilo.helpexpress.ui.entity.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(private val messageList: List<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid


    // ViewHolder para mensajes enviados
    class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
    }

    // ViewHolder para mensajes recibidos
    class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MESSAGE_SENT) {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_sent, parent, false)
            SentMessageViewHolder(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_received, parent, false)
            ReceivedMessageViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        if (holder is SentMessageViewHolder) {
            holder.messageTextView.text = currentMessage.message
            currentMessage.timestamp?.let { timestamp ->
                holder.timestampTextView.text = convertTimestampToString(timestamp)
            }
        } else if (holder is ReceivedMessageViewHolder) {
            holder.messageTextView.text = currentMessage.message
            currentMessage.timestamp?.let { timestamp ->
                holder.timestampTextView.text = convertTimestampToString(timestamp)
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    // Determinar si el mensaje fue enviado o recibido
    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        return if (message.senderId == currentUserId) MESSAGE_SENT else MESSAGE_RECEIVED
    }

    // Función para convertir el Timestamp a un formato legible
    private fun convertTimestampToString(timestamp: Timestamp): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = timestamp.toDate()  // Convertir el Timestamp a Date
        return sdf.format(date)
    }

    companion object {
        private const val MESSAGE_SENT = 1
        private const val MESSAGE_RECEIVED = 2
    }
}
