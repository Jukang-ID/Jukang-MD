package com.example.jukang.Firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.jukang.R
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.UpdateTokenFcm
import com.example.jukang.view.tukang.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFirebaseMessagingService :FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            sendNotification(it.title, it.body)
        }

        message.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + message.data)
            // Kamu bisa ambil data custom di sini, misal:
            // val transaksiId = remoteMessage.data["transaksi_id"]
        }
    }

    private fun sendRegistrationToServer(token: String) {
        Log.d(TAG, "Sending token to server (IMPLEMENT THIS!): $token")

        val pref = getSharedPreferences("AUTH", MODE_PRIVATE)
        val idpengguna = pref.getString("UID", "")
        val role = pref.getString("ROLE", "")

        if(role == "tukang"){
            val call = RetrofitClient.Jukang.updateFCM(idpengguna.toString(), token)
            call.enqueue(object : Callback<UpdateTokenFcm> {
                override fun onResponse(p0: Call<UpdateTokenFcm>, response: Response<UpdateTokenFcm>) {
                    if(response.isSuccessful){
                        Log.d("TAG", "onResponse: ${response.body()}")
                    }
                }

                override fun onFailure(p0: Call<UpdateTokenFcm>, p1: Throwable) {

                }

            })
        }

    }

    private fun sendNotification(title: String?, messageBody: String?) {
        // --- ANALOGI ---
        // Ini bagian "desain grafis".
        // Setelah "resepsionis" dapat info, bagian ini yang mendesain tampilan notifikasinya.
        // Mau icon apa, judulnya apa, isinya apa, semua diatur di sini.

        // Bikin Intent untuk membuka Activity tertentu saat notif diklik
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val channelId = "order_notification_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Buat Notification Channel (wajib untuk Android 8.0 Oreo ke atas)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notifikasi Pesanan", // Nama channel yang akan muncul di setting HP
                NotificationManager.IMPORTANCE_HIGH // Bikin notifnya muncul pop-up
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.aplikasijago) // Ganti dengan ikon notif kamu yg proper (biasanya putih transparan)
            .setContentTitle(title ?: "Ada Pesanan Baru!")
            .setContentText(messageBody ?: "Segera cek pesanan yang baru masuk.")
            .setAutoCancel(true) // Notif hilang setelah diklik
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent) // Aksi saat notif diklik

        // Tampilkan notifikasi dengan ID unik
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)

        sendRegistrationToServer(token)
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

}