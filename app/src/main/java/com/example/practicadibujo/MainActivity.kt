package com.example.practicadibujo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val squareSize = 100f

        val lienzo = findViewById<Lienzo>(R.id.lienzo)

        lienzo.post {
            val displayMetrics = resources.displayMetrics
            val screenWidth = (lienzo.width / squareSize).toInt() * squareSize.toInt()
            val screenHeight = (lienzo.height / squareSize).toInt() * squareSize.toInt()

            val layoutParams = lienzo.layoutParams
            layoutParams.width = screenWidth
            layoutParams.height = screenHeight
            lienzo.layoutParams = layoutParams
        }
    }
}