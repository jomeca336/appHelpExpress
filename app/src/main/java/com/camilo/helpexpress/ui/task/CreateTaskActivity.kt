package com.camilo.helpexpress.ui.task

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.camilo.helpexpress.R
import com.camilo.helpexpress.ui.entity.Notification
import com.camilo.helpexpress.ui.entity.Task
import com.camilo.helpexpress.ui.provider.NotificationProvider
import com.camilo.helpexpress.ui.provider.TaskProvider
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth

class CreateTaskActivity : AppCompatActivity() {

    lateinit var mTaskProvider: TaskProvider
    lateinit var mAuth: FirebaseAuth
    lateinit var selectedItem: String
    lateinit var mNotificationProvider: NotificationProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        mTaskProvider = TaskProvider()
        mAuth = FirebaseAuth.getInstance()
        mNotificationProvider = NotificationProvider()

        val titulo = findViewById<EditText>(R.id.titleEditText)
        val descripcion = findViewById<EditText>(R.id.descriptionEditText)
        val ubicacion = findViewById<EditText>(R.id.locationEditText)
        val precio = findViewById<EditText>(R.id.priceEditText)
        val pago = findViewById<CheckBox>(R.id.cashCheckBox)
        val crear = findViewById<Button>(R.id.createTaskButton)
        val categorias = findViewById<Spinner>(R.id.categorySpinner)

        var adapter = ArrayAdapter.createFromResource(
            this,
            R.array.opciones_spinner,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorias.adapter = adapter

        categorias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: android.view.View?, p2: Int, p3: Long) {
                selectedItem = categorias.selectedItem.toString()
                Toast.makeText(this@CreateTaskActivity, selectedItem, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        val goBackImageView = findViewById<ImageView>(R.id.goBackImageView)
        goBackImageView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        crear.setOnClickListener {
            if (titulo.text.isNotEmpty() && descripcion.text.isNotEmpty() && ubicacion.text.isNotEmpty() && precio.text.isNotEmpty() && selectedItem.isNotEmpty()) {

                // Crear tarea con Timestamp de Firebase
                val task = Task(
                    id = "",
                    uid = mAuth.currentUser!!.uid,
                    title = titulo.text.toString(),
                    description = descripcion.text.toString(),
                    category = selectedItem,
                    location = ubicacion.text.toString(),
                    price = precio.text.toString().toDouble(),
                    cash = pago.isChecked,
                    timestamp = Timestamp.now()
                )

                val noti = Notification("", mAuth.currentUser!!.uid, "Nueva tarea", "Se publico una nueva tarea cerca a tu zona", "Information")
                mTaskProvider.addTask(task).addOnSuccessListener {
                    Toast.makeText(this, "Tarea creada", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }.addOnFailureListener {
                    Toast.makeText(this, "Error al crear la tarea", Toast.LENGTH_SHORT).show()
                }

                mNotificationProvider.addNotification(noti).addOnSuccessListener {
                    Toast.makeText(this, "Notificacion creada", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Error al crear la notificacion", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Por favor llene todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
