package com.example.notificacionescursada2023

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btnIniciarServicio).setOnClickListener() {
            val intent = Intent(this, ServicioMusica::class.java)
            intent.putExtra("pause", true)
            ContextCompat.startForegroundService(this, intent)
        }

        findViewById<View>(R.id.btnStop).setOnClickListener() {
            val intent = Intent(this, ServicioMusica::class.java)
            stopService(intent)
        }

        /*

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Snackbar.make(
                findViewById<View>(android.R.id.content).rootView,
                "Aceptar Permisos",
                Snackbar.LENGTH_LONG
            ).show()
        }



        */

    }
}