package com.camilo.helpexpress.ui.profile_opt

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.camilo.helpexpress.R
import com.camilo.helpexpress.ui.history.ClientsFragment
import com.camilo.helpexpress.ui.history.EmployerActivity
import com.camilo.helpexpress.ui.history.JobsFragment

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Referencias a los LinearLayout
        val trabajosLinear = findViewById<LinearLayout>(R.id.trabajosLinear)
        val clientesLinear = findViewById<LinearLayout>(R.id.clientesLinear)
        val empleadoLinearLayout = findViewById<LinearLayout>(R.id.empleadoLinearLayout)
        val empleadorLinearLayout = findViewById<LinearLayout>(R.id.empleadorLinearLayout)

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        // Cargar el fragmento de Trabajos por defecto
        replaceFragment(JobsFragment())

        empleadoLinearLayout.setOnClickListener {
            empleadorLinearLayout.setBackgroundColor(Color.parseColor("#ECE8FF"))
            empleadoLinearLayout.setBackgroundColor(Color.parseColor("#4920F4"))
        }

        empleadorLinearLayout.setOnClickListener {
            empleadoLinearLayout.setBackgroundColor(Color.parseColor("#ECE8FF"))
            empleadorLinearLayout.setBackgroundColor(Color.parseColor("#4920F4"))
            val intent = Intent(this, EmployerActivity::class.java)
            startActivity(intent)
            finish()
        }

        trabajosLinear.setOnClickListener {
            clientesLinear.setBackgroundColor(Color.parseColor("#ECE8FF"))
            trabajosLinear.setBackgroundColor(Color.parseColor("#D9D0FE"))
            replaceFragment(JobsFragment())  // Reemplazar con el fragmento de Trabajos
        }

        // Listener para Clientes
        clientesLinear.setOnClickListener {
            trabajosLinear.setBackgroundColor(Color.parseColor("#ECE8FF"))
            clientesLinear.setBackgroundColor(Color.parseColor("#D9D0FE"))
            replaceFragment(ClientsFragment())  // Reemplazar con el fragmento de Clientes
        }
    }

    // Función para reemplazar el fragmento
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
