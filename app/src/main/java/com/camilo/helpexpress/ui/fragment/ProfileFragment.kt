package com.camilo.helpexpress.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.camilo.helpexpress.R
import com.camilo.helpexpress.ui.profile_opt.HistoryActivity
import com.camilo.helpexpress.ui.profile_opt.PaymentMethods
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var descriptionTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Inicializar FirebaseAuth y Firestore
        mAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Obtener referencias a los TextViews
        usernameTextView = view.findViewById(R.id.usernameTextView)
        emailTextView = view.findViewById(R.id.emailTextView)
        descriptionTextView = view.findViewById(R.id.descriptionTextView)

        loadUserInfo()

        // Configurar listeners para navegar a otras actividades
        val historyImageView = view.findViewById<CardView>(R.id.historyCard)
        historyImageView.setOnClickListener {
            val intent = Intent(requireContext(), HistoryActivity::class.java)
            startActivity(intent)
        }

        val paymentImageView = view.findViewById<CardView>(R.id.paymentCard)
        paymentImageView.setOnClickListener {
            val intent = Intent(requireContext(), PaymentMethods::class.java)
            startActivity(intent)
        }

        val personalImageView = view.findViewById<CardView>(R.id.infoCard)
        personalImageView.setOnClickListener {
            val intent = Intent(requireContext(), com.camilo.helpexpress.ui.profile_opt.PersonalInfoActivity::class.java)
            startActivity(intent)
        }

        val cerrarSesionImageView = view.findViewById<CardView>(R.id.cerrarSesionCard)
        cerrarSesionImageView.setOnClickListener {
            mAuth.signOut()
            Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), com.camilo.helpexpress.ui.auth.LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }

    private fun loadUserInfo() {
        val user: FirebaseUser? = mAuth.currentUser
        if (user != null) {
            // Obtener el UID del usuario actual
            val uid = user.uid

            // Obtener los datos del usuario desde Firestore
            val userDocRef = firestore.collection("usuarios").document(uid)
            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Obtener el nombre y la descripción desde Firestore
                        val username = document.getString("nombre") ?: "Usuario"
                        val description = document.getString("descripcion") ?: "Sin descripción"

                        // Actualizar los TextViews
                        usernameTextView.text = username
                        descriptionTextView.text = description

                        // El correo lo podemos obtener desde Firebase Auth
                        emailTextView.text = user.email ?: "Correo no disponible"
                    } else {
                        Toast.makeText(requireContext(), "No se encontraron los datos del usuario", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error al cargar los datos del usuario", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "No se pudo obtener la información del usuario", Toast.LENGTH_SHORT).show()
        }
    }
}
