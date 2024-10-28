package com.camilo.helpexpress.ui.chat

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.camilo.helpexpress.R
import com.google.firebase.firestore.FirebaseFirestore

class UserDetailsActivity : AppCompatActivity() {

    private lateinit var userId: String
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var ageTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        // Obtener el userId desde el intent
        userId = intent.getStringExtra("userId") ?: ""

        // Inicializar los TextViews
        nameTextView = findViewById(R.id.userNameTextView)
        emailTextView = findViewById(R.id.userEmailTextView)
        phoneTextView = findViewById(R.id.userPhoneTextView)
        descriptionTextView = findViewById(R.id.userDescriptionTextView)
        ageTextView = findViewById(R.id.ageTextView)

        val goBackImageView = findViewById<ImageView>(R.id.goBackImageView)
        goBackImageView.setOnClickListener {
            finish()
        }

        // Cargar los detalles del usuario
        loadUserDetails()
    }

    private fun loadUserDetails() {
        if (userId.isNotEmpty()) {
            firestore.collection("usuarios").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val name = document.getString("nombre") ?: "No disponible"
                        val email = document.getString("correo") ?: "No disponible"
                        val phone = document.getString("telefono") ?: "No disponible"
                        val age = document.getString("edad") ?: "No disponible"
                        val description = document.getString("descripcion") ?: "No disponible"

                        // Mostrar los detalles en los TextViews
                        nameTextView.text = name
                        emailTextView.text = email
                        phoneTextView.text = phone
                        descriptionTextView.text = description
                        ageTextView.text = age
                    } else {
                        Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al cargar los detalles", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "ID de usuario no válido", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
