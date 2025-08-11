package com.example.jukang.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.jukang.R

fun createNotificationChannel(CHANNEL_ID: String, context: Context) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Saluran Notifikasi Umum"
        val descriptionText =
            "Saluran ini digunakan untuk notifikasi umum aplikasi." // Deskripsi saluran
        val importance = NotificationManager.IMPORTANCE_HIGH // Tingkat kepentingan notifikasi

        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

@SuppressLint("MissingPermission")
fun showNotification(
    context: Context,
    CHANNEL_ID: String,
    NOTIFICATION_ID: Int,
    title: String,
    SubTitle: String
) {

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.aplikasijago) // Ikon kecil yang akan ditampilkan di status bar
        .setContentTitle(title) // Judul notifikasi
        .setContentText(SubTitle) // Teks notifikasi
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setDefaults(NotificationCompat.DEFAULT_ALL)
        // .setContentIntent(pendingIntent) // Atur PendingIntent jika notifikasi harus melakukan sesuatu saat diketuk
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)) {
        notify(NOTIFICATION_ID, builder.build())
    }
}