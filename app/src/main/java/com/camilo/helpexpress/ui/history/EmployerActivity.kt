package com.camilo.helpexpress.ui.history

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.camilo.helpexpress.R
import com.camilo.helpexpress.ui.profile_opt.HistoryActivity

class EmployerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empleador)

        // Referencias a los LinearLayout
        val publiLinear = findViewById<LinearLayout>(R.id.publiLinear)
        val trabajadoresLinear = findViewById<LinearLayout>(R.id.trabajadoresLinear)
        val empleadoLinearLayout = findViewById<LinearLayout>(R.id.empleadoLinearLayout)
        val empleadorLinearLayout = findViewById<LinearLayout>(R.id.empleadorLinearLayout)

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        // Cargar el fragmento de Trabajos por defecto
        replaceFragment(PubliFragment())

        empleadoLinearLayout.setOnClickListener {
            empleadorLinearLayout.setBackgroundColor(Color.parseColor("#ECE8FF"))
            empleadoLinearLayout.setBackgroundColor(Color.parseColor("#4920F4"))
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
            finish()
        }

        empleadorLinearLayout.setOnClickListener {
            empleadoLinearLayout.setBackgroundColor(Color.parseColor("#ECE8FF"))
            empleadorLinearLayout.setBackgroundColor(Color.parseColor("#4920F4"))
        }

        publiLinear.setOnClickListener {
            trabajadoresLinear.setBackgroundColor(Color.parseColor("#ECE8FF"))
            publiLinear.setBackgroundColor(Color.parseColor("#D9D0FE"))
            replaceFragment(PubliFragment())  // Reemplazar con el fragmento de Trabajos
        }

        // Listener para Clientes
        trabajadoresLinear.setOnClickListener {
            publiLinear.setBackgroundColor(Color.parseColor("#ECE8FF"))
            trabajadoresLinear.setBackgroundColor(Color.parseColor("#D9D0FE"))
            replaceFragment(WorkersFragment())  // Reemplazar con el fragmento de Clientes
        }
    }

    // Función para reemplazar el fragmento
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}