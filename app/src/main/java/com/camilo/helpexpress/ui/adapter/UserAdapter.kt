package com.camilo.helpexpress.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.camilo.helpexpress.R
import com.camilo.helpexpress.ui.entity.User

class UserAdapter(private val users: List<User>, private val onUserClicked: (User) -> Unit) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userNameTextView: TextView = view.findViewById(R.id.userNameTextView)
        val userDescriptionTextView: TextView = view.findViewById(R.id.userDescriptionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]

        // Asignar el nombre y descripción del usuario a los TextViews
        holder.userNameTextView.text = user.nombre
        holder.userDescriptionTextView.text = user.descripcion

        // Al hacer clic en un usuario, se crea un chat
        holder.itemView.setOnClickListener {
            onUserClicked(user)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}
