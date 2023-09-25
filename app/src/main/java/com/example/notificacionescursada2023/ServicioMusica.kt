package com.example.notificacionescursada2023

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ServicioMusica : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private val CHANNEL_ID: String = "Musica"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificacion = crearNotificacion()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            crearChannelId()
            startForeground(1, notificacion)
        } else{
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            } else {
                NotificationManagerCompat.from(this).notify(1, notificacion)
            }
        }

        if(intent?.getBooleanExtra("stop", false) == true){
            stop()
        } else{
            //configurar el reproductor si no se configuro
            if(mediaPlayer == null){
                configurarMediaPlayer()
            }
            if(intent?.getBooleanExtra("pause", false) == true){
                if(mediaPlayer?.isPlaying == true){
                    mediaPlayer?.pause()
                } else{
                    mediaPlayer?.start()
                }
            } else{
                mediaPlayer?.start()
            }
        }

        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun crearChannelId() {
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(CHANNEL_ID, "Servicio de Musica", importance)
        channel.description = "Se mostrara al reproducir musica"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun crearNotificacion(): Notification {
        val intentPause = Intent(this, ServicioMusica::class.java)
        intentPause.putExtra("pause", true)
        val piPause = PendingIntent
            .getService(this, 1, intentPause, PendingIntent.FLAG_UPDATE_CURRENT)

        val pauseAction = NotificationCompat.Action.Builder(
            R.drawable.baseline_play_arrow_24, "Play/Pause", piPause
        ).build()

        val intentStop = Intent(this, ServicioMusica::class.java)
        intentStop.putExtra("stop", true)
        val piStopService = PendingIntent
            .getService(this, 2, intentStop, PendingIntent.FLAG_UPDATE_CURRENT)

        val stopAction = NotificationCompat.Action.Builder(
            R.drawable.baseline_stop_24, "Stop", piStopService
        ).build()

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Reproduciendo Musica")
            .addAction(pauseAction)
            .addAction(stopAction)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSmallIcon(R.drawable.baseline_play_arrow_24)
            .build()
    }


    private fun configurarMediaPlayer(){
        mediaPlayer = MediaPlayer.create(this, R.raw.musica)
        mediaPlayer?.setOnCompletionListener {
            stop()
        }
    }

    private fun stop() {
        stopNotificacion()
        stopMusic()
        stopSelf()
    }

    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun stopNotificacion(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            NotificationManagerCompat.from(this).cancel(1)
        }
    }

    override fun onDestroy() {
        stopMusic()
        stopNotificacion()
        super.onDestroy()
    }

}