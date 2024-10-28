package com.camilo.helpexpress.ui.profile_opt

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.camilo.helpexpress.R

class PaymentMethods : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_methods)

        val goBackImageView = findViewById<ImageView>(R.id.goBackImageView)
        goBackImageView.setOnClickListener {
            finish()
        }
    }
}