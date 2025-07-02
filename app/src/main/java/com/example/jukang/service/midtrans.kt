package com.example.jukang.service

import android.app.Application
import android.os.Build
import com.example.jukang.BuildConfig
import com.midtrans.sdk.uikit.external.UiKitApi

class midtrans :Application() {
    override fun onCreate() {
        super.onCreate()
    }

    private fun buildUiKit(){
        UiKitApi.Builder()
            .withContext(this)
            .withMerchantUrl("https://api-jukang.vercel.app/")
            .withMerchantClientKey(BuildConfig.CLIENT_KEY_MT)
            .enableLog(true)
            .build()
    }
}