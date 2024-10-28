package com.camilo.helpexpress.ui.profile_opt

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.camilo.helpexpress.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PersonalInfoActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String

    private lateinit var nameTextView: TextView
    private lateinit var ageTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var descriptionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_info)

        // Inicializar Firebase
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Obtener el ID del usuario actual
        userId = auth.currentUser?.uid ?: ""

        // Inicializar las vistas
        nameTextView = findViewById(R.id.nameUser)
        ageTextView = findViewById(R.id.ageUser)
        addressTextView = findViewById(R.id.addresUser)
        phoneTextView = findViewById(R.id.phoneUser)
        descriptionTextView = findViewById(R.id.descriptionUser)

        val editNameImageView = findViewById<ImageView>(R.id.editNameImageView)
        val editAgeImageView = findViewById<ImageView>(R.id.editAgeImageView)
        val editAddressImageView = findViewById<ImageView>(R.id.editAdresImageView)
        val editPhoneImageView = findViewById<ImageView>(R.id.editPhoneImageView)
        val editDescriptionImageView = findViewById<ImageView>(R.id.editDescriptionImageView)
        val goBackImageView = findViewById<ImageView>(R.id.goBackImageView)
        goBackImageView.setOnClickListener {
            finish()
        }

        // Cargar datos del usuario
        loadUserData()

        // Editar el nombre
        editNameImageView.setOnClickListener {
            showEditDialog("nombre", "Nombre", nameTextView)
        }

        // Editar la edad
        editAgeImageView.setOnClickListener {
            showEditDialog("edad", "Edad", ageTextView)
        }

        // Editar la dirección
        editAddressImageView.setOnClickListener {
            showEditDialog("direccion", "Dirección", addressTextView)
        }

        // Editar el teléfono
        editPhoneImageView.setOnClickListener {
            showEditDialog("telefono", "Teléfono", phoneTextView)
        }
        editDescriptionImageView.setOnClickListener {
            showEditDialog("descripcion", "Descripción", descriptionTextView)
        }
    }

    // Función para cargar los datos del usuario desde Firestore
    private fun loadUserData() {
        val docRef = firestore.collection("usuarios").document(userId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    // Mostrar los datos en las vistas
                    nameTextView.text = document.getString("nombre")
                    ageTextView.text = document.getString("edad") ?: "Indefinida"
                    addressTextView.text = document.getString("direccion") ?: "Indefinida"
                    phoneTextView.text = document.getString("telefono") ?: "Indefinido"
                    descriptionTextView.text = document.getString("descripcion") ?: "Indefinida"

                } else {
                    Toast.makeText(this, "No se encontró el usuario", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar datos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    // Función para mostrar un cuadro de diálogo para editar el dato del usuario
    // Función para mostrar un cuadro de diálogo para editar el dato del usuario
    private fun showEditDialog(field: String, fieldLabel: String, textView: TextView) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar $fieldLabel")

        // Usar EditText en lugar de TextView para permitir la edición
        val input = EditText(this)
        input.setText(textView.text.toString())  // Inicializar el EditText con el valor actual

        builder.setView(input)
        builder.setPositiveButton("Guardar") { _, _ ->
            var newValue = input.text.toString().trim()

            if (newValue.isNotEmpty()) {
                // Si estamos editando la edad, concatenamos "años"
                if (field == "edad") {
                    newValue = "$newValue años"
                }

                updateUserData(field, newValue)
                textView.text = newValue
            } else {
                Toast.makeText(this, "El campo no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }


    // Función para actualizar los datos del usuario en Firestore
    private fun updateUserData(field: String, value: String) {
        val docRef = firestore.collection("usuarios").document(userId)
        docRef.update(field, value)
            .addOnSuccessListener {
                Toast.makeText(this, "$field actualizado correctamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al actualizar $field: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
