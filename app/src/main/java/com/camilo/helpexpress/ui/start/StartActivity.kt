package com.camilo.helpexpress.ui.start

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.camilo.helpexpress.R
import com.camilo.helpexpress.ui.auth.LoginActivity
import com.camilo.helpexpress.ui.task.MainActivity
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start)

        val mAuth = FirebaseAuth.getInstance()
        val splashTimeOut: Long = 3000
        val logoImageView= findViewById<ImageView>(R.id.logoImageView)
        val welcomeTextView = findViewById<TextView>(R.id.welcomeTextView)


        // Crear la animación de vibración
        val animator = ObjectAnimator.ofFloat(logoImageView, "translationX", 0f, 20f)
        animator.duration = 200 // Duración de la animación
        animator.repeatCount = 5 // Número de repeticiones
        animator.repeatMode = ObjectAnimator.REVERSE // Volver al valor original
        animator.start() // Iniciar la animación

        val animator2 = ObjectAnimator.ofFloat(welcomeTextView, "translationX", 0f, 20f)
        animator2.duration = 200 // Duración de la animación
        animator2.repeatCount = 5 // Número de repeticiones
        animator2.repeatMode = ObjectAnimator.REVERSE // Volver al valor original
        animator2.start() // Iniciar la animación

        Handler(Looper.getMainLooper()).postDelayed({
            if (mAuth.currentUser != null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, splashTimeOut)
    }
}