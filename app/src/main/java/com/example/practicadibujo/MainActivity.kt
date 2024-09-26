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

        // Ajustar los insets para la pantalla completa edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Tamaño de los bloques (debe coincidir con el de la serpiente)
        val squareSize = 100f

        // Referencia al Lienzo definido en el XML
        val lienzo = findViewById<Lienzo>(R.id.lienzo)

        // Calcula el tamaño disponible sin las barras del sistema y ajusta el Lienzo
        lienzo.post {
            val displayMetrics = resources.displayMetrics
            val screenWidth = (lienzo.width / squareSize).toInt() * squareSize.toInt()
            val screenHeight = (lienzo.height / squareSize).toInt() * squareSize.toInt()

            // Ajusta el tamaño del Lienzo a un múltiplo del tamaño del bloque
            val layoutParams = lienzo.layoutParams
            layoutParams.width = screenWidth
            layoutParams.height = screenHeight
            lienzo.layoutParams = layoutParams
        }
    }
}