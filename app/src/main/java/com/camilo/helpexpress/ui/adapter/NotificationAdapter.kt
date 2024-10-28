package com.camilo.helpexpress.ui.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.camilo.helpexpress.R
import com.camilo.helpexpress.ui.entity.Notification


class NotificationAdapter(context: Context,  private var notifications: MutableList<Notification>): RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {




    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val titleTextView = view.findViewById<TextView>(R.id.titleNotificationTextView)
        val descriptionTextView = view.findViewById<TextView>(R.id.descriptionNotificationTextView)
        val iconImageView = view.findViewById<ImageView>(R.id.iconNotificationImageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.notification, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTextView.text = notifications[position].title
        holder.descriptionTextView.text = notifications[position].description
        holder.iconImageView.setImageResource(R.drawable.happy_face_ic)

    }

    override fun getItemCount(): Int {
        return notifications.size
    }



}