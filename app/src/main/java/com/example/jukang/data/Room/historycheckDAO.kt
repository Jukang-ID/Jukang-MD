package com.example.jukang.data.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface historycheckDAO {
    @Insert
    suspend fun inserhistory(data:historyRiwayat)

    @Query("SELECT * FROM riwayat WHERE namaUser = :namaUser AND tanggal = :tanggal LIMIT 1")
    suspend fun checkHistory(namaUser:String, tanggal:String):historyRiwayat
}

