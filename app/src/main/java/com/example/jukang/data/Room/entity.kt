package com.example.jukang.data.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "riwayat")
data class historyRiwayat(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val namaUser : String,
    val tanggal : String,
)