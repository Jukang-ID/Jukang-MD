package com.example.jukang.view.maps

import android.app.Application
import org.osmdroid.config.Configuration

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Configuration.getInstance().load(this, getSharedPreferences("osmandroid", MODE_PRIVATE))
    }
}