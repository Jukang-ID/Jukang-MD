package com.example.jukang.view.maps

import android.app.Application
import android.util.Log
import com.example.jukang.BuildConfig
import com.midtrans.sdk.uikit.external.UiKitApi
import org.osmdroid.config.Configuration

class MainApplication : Application() {

    companion object {
        var uiKitApi: UiKitApi? = null
    }

    override fun onCreate() {
        super.onCreate()

        buildUiKit()

        Configuration.getInstance().load(this, getSharedPreferences("osmandroid", MODE_PRIVATE))
    }

    private fun buildUiKit() {
        Log.d("PaymentFlowDebug", "MainApplication buildUiKit() dipanggil.")
        try {
            uiKitApi = UiKitApi.Builder()
                .withContext(this)
                .withMerchantUrl("https://api-jukang.vercel.app/")
                .withMerchantClientKey("SB-Mid-client-VDwh2ii4glpQuWkU")
                .enableLog(true)
                .build()
            Log.d("PaymentFlowDebug", "MainApplication buildUiKit() BERHASIL. uiKitApi sekarang sudah diinisialisasi.")
        } catch (e: Exception) {
            Log.e("PaymentFlowDebug", "FATAL ERROR saat buildUiKit di MainApplication: ${e.message}", e)
            uiKitApi = null // Pastikan null jika gagal
        }
    }
}