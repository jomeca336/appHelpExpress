package com.camilo.helpexpress.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.camilo.helpexpress.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        // Inicializar FirebaseAuth y Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val nameEditText = findViewById<EditText>(R.id.nameRegisterEditText)
        val emailEditText = findViewById<EditText>(R.id.emailRegisterEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordRegisterEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Validaciones de los campos
            if (name.isEmpty()) {
                nameEditText.error = "Por favor, ingresa tu nombre"
                nameEditText.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                emailEditText.error = "Por favor, ingresa un correo electrónico"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.error = "Por favor, ingresa un correo válido"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordEditText.error = "Por favor, ingresa una contraseña"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                passwordEditText.error = "La contraseña debe tener al menos 6 caracteres"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            // Llamar a la función de registro de usuario
            registerUser(name, email, password)
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {
                                // Aquí se hace el registro exitoso y se añade a Firestore
                                addUserToFirestore(user.uid, name, email)

                                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Error al actualizar el perfil", Toast.LENGTH_LONG).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Error en el registro: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    // Función para agregar el usuario a la colección "usuarios" en Firestore
    private fun addUserToFirestore(userId: String, name: String, email: String) {
        // Crear un mapa con los datos del usuario
        val user = hashMapOf(
            "nombre" to name,
            "descripcion" to "Indefinido",
            "correo" to email,
            "uid" to userId,
            "edad" to "Indefinida",        // Edad indefinida por defecto
            "direccion" to "Indefinida",   // Dirección indefinida por defecto
            "telefono" to "Indefinido"     // Número de teléfono indefinido por defecto
        )

        // Agregar el usuario a la colección "usuarios"
        firestore.collection("usuarios").document(userId)
            .set(user)
            .addOnSuccessListener {
                // El usuario fue agregado exitosamente a Firestore
                Toast.makeText(this, "Usuario agregado a Firestore", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Hubo un error al agregar el usuario
                Toast.makeText(this, "Error al agregar usuario a Firestore: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
