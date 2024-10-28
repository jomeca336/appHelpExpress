package com.camilo.helpexpress.ui.task

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camilo.helpexpress.R
import com.camilo.helpexpress.ui.adapter.NotificationAdapter
import com.camilo.helpexpress.ui.entity.Notification
import com.camilo.helpexpress.ui.provider.NotificationProvider
import com.google.firebase.auth.FirebaseAuth

class NotificationsActivity : AppCompatActivity() {
    lateinit var mNotificationProvider: NotificationProvider
    lateinit var notifications: MutableList<Notification>
    lateinit var mAuth: FirebaseAuth
    lateinit var notificationsRecyclerView: RecyclerView
    lateinit var noNotificationsTextView: TextView
    lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)


        mNotificationProvider = NotificationProvider()
        notifications = mutableListOf()
        mAuth = FirebaseAuth.getInstance()


        notificationsRecyclerView = findViewById(R.id.notificationsRecyclerView)
        noNotificationsTextView = findViewById(R.id.noNotificationsTextView)


        notificationsRecyclerView.layoutManager = LinearLayoutManager(this)


        adapter = NotificationAdapter(this, notifications)
        notificationsRecyclerView.adapter = adapter


        val goBackImageView = findViewById<ImageView>(R.id.goBackImageView)
        goBackImageView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        loadNotifications()
    }

    private fun loadNotifications() {

        mNotificationProvider.getNotifications().get()
            .addOnSuccessListener { documents ->
                notifications.clear()
                for (document in documents) {
                    val notification = document.toObject(Notification::class.java)
                    notifications.add(notification)
                }


                if (notifications.isEmpty()) {
                    noNotificationsTextView.visibility = View.VISIBLE
                    notificationsRecyclerView.visibility = View.GONE
                } else {
                    noNotificationsTextView.visibility = View.GONE
                    notificationsRecyclerView.visibility = View.VISIBLE
                }


                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                noNotificationsTextView.text = "Error al cargar notificaciones"
                noNotificationsTextView.visibility = View.VISIBLE
                notificationsRecyclerView.visibility = View.GONE
            }
    }
}
