package com.camilo.helpexpress.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.camilo.helpexpress.R
import com.camilo.helpexpress.ui.entity.Client

class ClientAdapter(private val clients: List<Client>) :
    RecyclerView.Adapter<ClientAdapter.ClientViewHolder>() {

    class ClientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clientProfileImage: ImageView = itemView.findViewById(R.id.clientProfileImage)
        val clientNameTextView: TextView = itemView.findViewById(R.id.clienteName)
        val clientPhoneTextView: TextView = itemView.findViewById(R.id.clientePhone)
        val clientEmailTextView: TextView = itemView.findViewById(R.id.clienteEmail)
        val clientStatusButton: Button = itemView.findViewById(R.id.clienteStatusButton)
        val chatIcon: ImageView = itemView.findViewById(R.id.chatIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ClientViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return clients.size
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        val client = clients[position]
        holder.clientNameTextView.text = client.name
        holder.clientPhoneTextView.text = client.phone
        holder.clientEmailTextView.text = client.email

        // Cambiar el texto y el color del botón de estado
        holder.clientStatusButton.text = client.status
        holder.clientStatusButton.setTextColor(if (client.status == "Sin puntuar") Color.RED else Color.GREEN)

        // Acciones al hacer clic en el icono de chat
        holder.chatIcon.setOnClickListener {
            // Aquí puedes manejar la acción de abrir el chat
        }
    }
}
